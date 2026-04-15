package com.eoms.app;

import com.eoms.Boundary.AdminReportView;
import com.eoms.observer.EmailNotificationListener;
import com.eoms.observer.OrderEventManager;
import com.eoms.observer.OrderEventType;
import com.eoms.observer.SmsNotificationListener;
import com.eoms.observer.WhatsAppNotificationListener;
import com.eoms.Boundary.CheckoutView;
import com.eoms.adapter.DHLShippingAdapter;
import com.eoms.Boundary.OrderTrackingView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.DAO.OrderInterface;
import com.eoms.DAO.PaymentInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.DAO.ShipmentDAO;
import com.eoms.DAO.ProductInterface;
import com.eoms.DAO.OrderDAO;
import com.eoms.DAO.PaymentDAO;
import com.eoms.DAO.ProductDAO;
import com.eoms.service.OrderProcessingMediator;
import com.eoms.service.impl.OrderProcessingMediatorImpl;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.service.ProductService;
import com.eoms.service.ReportService;
import com.eoms.service.ShippingService;
import com.eoms.bridge_notification.EmailSender;
import com.eoms.bridge_notification.MessageSender;
import com.eoms.bridge_notification.Notification;
import com.eoms.bridge_notification.OrderConfirmationNotification;
import com.eoms.bridge_notification.PaymentReceiptNotification;
import com.eoms.bridge_notification.ShippingUpdateNotification;
import com.eoms.bridge_notification.SmsSender;
import com.eoms.bridge_notification.WhatsAppSender;
import com.eoms.bridge_notification.decorator.TimestampMessageSenderDecorator;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.service.ProductService;
import com.eoms.service.ReportService;
import com.eoms.service.ShippingService;
import com.eoms.service.impl.OrderServiceImpl;
import com.eoms.service.impl.PaymentServiceImpl;
import com.eoms.service.impl.ProductServiceImpl;
import com.eoms.service.impl.ReportServiceImpl;
import com.eoms.config.SingletonInitializer;

import java.util.Scanner;
import com.eoms.util.InputValidator;
import com.eoms.app.PaymentProcessorProvider;

/**
 * This class wires dependencies and exposes a single run() method.
 */
public class EomsApplication {
    private final Scanner scanner;
    private final ProductCatalogView catalogView;
    private final CheckoutView checkoutView;
    private final PaymentView paymentView;
    private final OrderTrackingView trackingView;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final Notification orderConfirmationNotification;
    private final Notification paymentReceiptNotification;
    private final Notification shippingUpdateNotification;
    private final ProductInterface productDAO;
    private final AdminReportView adminReportView;
    private final OrderProcessingMediator orderProcessingMediator;

    public EomsApplication(Scanner scanner) {
        this.scanner = scanner;

        // DAOs
        OrderInterface orderDAO = new OrderDAO();
        PaymentInterface paymentDAO = new PaymentDAO();
        ShipmentInterface shipmentDAO = new ShipmentDAO();

        this.productDAO = new ProductDAO();

        
        // services
        ProductService productService = new ProductServiceImpl(productDAO);
        ReportService reportService = new ReportServiceImpl(productService);
        this.orderService = new OrderServiceImpl(orderDAO, productDAO);
        this.paymentService = new PaymentServiceImpl(paymentDAO);
        ShippingService shippingService = new DHLShippingAdapter();

        // Mediator
        this.orderProcessingMediator = new OrderProcessingMediatorImpl(orderService, paymentService, shippingService, paymentDAO, shipmentDAO);

        // Decorator: timestamp on email/WhatsApp bodies for order notifications and email receipts.
        MessageSender emailSender = new TimestampMessageSenderDecorator(new EmailSender());
        MessageSender whatsAppSender = new TimestampMessageSenderDecorator(new WhatsAppSender());
        MessageSender orderConfirmationChannel = message -> {
            emailSender.sendMessage(message);
            whatsAppSender.sendMessage(message);
        };

        this.orderConfirmationNotification = new OrderConfirmationNotification(orderConfirmationChannel);
        this.paymentReceiptNotification = new PaymentReceiptNotification(emailSender);

        // Decorator: timestamp on SMS shipping updates.
        MessageSender smsSender = new TimestampMessageSenderDecorator(new SmsSender());
        this.shippingUpdateNotification = new ShippingUpdateNotification(smsSender);

        // views
        this.catalogView = new ProductCatalogView(productService, scanner);
        this.adminReportView = new AdminReportView(reportService, scanner);
        this.checkoutView = new CheckoutView(orderService, scanner);
        this.paymentView = new PaymentView(paymentService, scanner);
        this.trackingView = new OrderTrackingView(shippingService, scanner, shippingUpdateNotification);
        OrderEventManager eventManager = OrderEventManager.getInstance();

     // Create listeners using existing senders
        EmailNotificationListener emailListener = new EmailNotificationListener(emailSender);
        SmsNotificationListener smsListener = new SmsNotificationListener(smsSender);
        WhatsAppNotificationListener whatsAppListener = new WhatsAppNotificationListener(whatsAppSender);

        // ORDER CREATED
        eventManager.subscribe(OrderEventType.ORDER_CREATED, emailListener);
        eventManager.subscribe(OrderEventType.ORDER_CREATED, smsListener);
        eventManager.subscribe(OrderEventType.ORDER_CREATED, whatsAppListener);

        // ORDER FINALIZED
        eventManager.subscribe(OrderEventType.ORDER_FINALIZED, emailListener);
        eventManager.subscribe(OrderEventType.ORDER_FINALIZED, smsListener);
        eventManager.subscribe(OrderEventType.ORDER_FINALIZED, whatsAppListener);

        // ORDER PAID
        eventManager.subscribe(OrderEventType.ORDER_PAID, emailListener);
        eventManager.subscribe(OrderEventType.ORDER_PAID, smsListener);
        eventManager.subscribe(OrderEventType.ORDER_PAID, whatsAppListener);
    }

    public void run() {
        // Initialize all singletons once at startup
        SingletonInitializer.initialize();

        boolean running = true;
        while (running) {
            System.out.println("=== E-OMS Demo ===");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("0. Exit");

            int role;
            if (scanner.hasNextInt()) {
                role = scanner.nextInt();
                InputValidator.validateRange(role, 0, 2, "Role choice");
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            switch (role) {
                case 0:
                    running = false;
                    break;
                case 1:
                    AdminRoleHandler adminHandler = new AdminRoleHandler(catalogView, adminReportView);
                    adminHandler.handle(scanner);
                    break;
                case 2:
                    PaymentProcessorProvider paymentProcessorProvider = new DefaultPaymentProcessorProvider();
                    new CustomerRoleHandler(
                            catalogView,
                            checkoutView,
                            paymentView,
                            trackingView,
                            orderService,
                            orderConfirmationNotification,
                            paymentReceiptNotification,
                            paymentProcessorProvider,
                            orderProcessingMediator)
                            .handle(scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
