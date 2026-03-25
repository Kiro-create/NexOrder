package com.eoms.app;

import com.eoms.Boundary.AdminReportView;
import com.eoms.Boundary.CheckoutView;
import com.eoms.adapter.DHLShippingAdapter;
import com.eoms.Boundary.OrderTrackingView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.DAO.OrderInterface;
import com.eoms.DAO.PaymentInterface;
import com.eoms.DAO.ProductInterface;
import com.eoms.DAO.OrderDAO;
import com.eoms.DAO.PaymentDAO;
import com.eoms.DAO.ProductDAO;
import com.eoms.bridge_notification.EmailSender;
import com.eoms.bridge_notification.MessageSender;
import com.eoms.bridge_notification.Notification;
import com.eoms.bridge_notification.OrderConfirmationNotification;
import com.eoms.bridge_notification.PaymentReceiptNotification;
import com.eoms.bridge_notification.ShippingUpdateNotification;
import com.eoms.bridge_notification.SmsSender;
import com.eoms.bridge_notification.decorator.SmsLengthLimitMessageSenderDecorator;
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

import java.util.Scanner;

/**
 * High-level entry point logic extracted from Main to reduce its complexity.
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

    public EomsApplication(Scanner scanner) {
        this.scanner = scanner;

        // DAOs
        OrderInterface orderDAO = new OrderDAO();
        PaymentInterface paymentDAO = new PaymentDAO();

        this.productDAO = new ProductDAO();

        
        // services
        ProductService productService = new ProductServiceImpl(productDAO);
        ReportService reportService = new ReportServiceImpl(productService);
        this.orderService = new OrderServiceImpl(orderDAO, productDAO);
        this.paymentService = new PaymentServiceImpl(paymentDAO);
        ShippingService shippingService = new DHLShippingAdapter();

        // Decorator: timestamp on email bodies for audit (order/payment notifications).
        MessageSender emailSender = new TimestampMessageSenderDecorator(new EmailSender());
        this.orderConfirmationNotification = new OrderConfirmationNotification(emailSender);
        this.paymentReceiptNotification = new PaymentReceiptNotification(emailSender);

        // Decorator: SMS length limit for shipping alerts.
        MessageSender shippingChannel = new SmsLengthLimitMessageSenderDecorator(new SmsSender());
        this.shippingUpdateNotification = new ShippingUpdateNotification(shippingChannel);

        // views
        this.catalogView = new ProductCatalogView(productService, scanner);
        this.adminReportView = new AdminReportView(reportService, scanner);
        this.checkoutView = new CheckoutView(orderService, scanner);
        this.paymentView = new PaymentView(paymentService, scanner);
        this.trackingView = new OrderTrackingView(shippingService, scanner, shippingUpdateNotification);
    }


    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("=== E-OMS Demo ===");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("0. Exit");

            int role;
            if (scanner.hasNextInt()) {
                role = scanner.nextInt();
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
                    new CustomerRoleHandler(
                            catalogView,
                            checkoutView,
                            paymentView,
                            trackingView,
                            orderService,
                            paymentService,
                            orderConfirmationNotification,
                            paymentReceiptNotification)
                            .handle(scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
