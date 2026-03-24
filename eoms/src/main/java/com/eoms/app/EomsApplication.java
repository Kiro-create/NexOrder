package com.eoms.app;

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
import com.eoms.bridge_notification.decorator.DeliveryConfirmationSenderDecorator;
import com.eoms.bridge_notification.decorator.LoggingMessageSenderDecorator;
import com.eoms.bridge_notification.decorator.MessageHistorySenderDecorator;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.service.ProductService;
import com.eoms.service.ShippingService;
import com.eoms.service.impl.OrderServiceImpl;
import com.eoms.service.impl.PaymentServiceImpl;
import com.eoms.service.impl.ProductServiceImpl;

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

    public EomsApplication(Scanner scanner) {
        this.scanner = scanner;

        // DAOs
        ProductInterface productDAO = new ProductDAO();
        OrderInterface orderDAO = new OrderDAO();
        PaymentInterface paymentDAO = new PaymentDAO();
        
        // services
        ProductService productService = new ProductServiceImpl(productDAO);
        this.orderService = new OrderServiceImpl(orderDAO, productDAO);
        this.paymentService = new PaymentServiceImpl(paymentDAO);
        ShippingService shippingService = new DHLShippingAdapter();

        MessageSender decoratedSender = new MessageHistorySenderDecorator(
                new DeliveryConfirmationSenderDecorator(
                        new LoggingMessageSenderDecorator(
                                new EmailSender())));
        this.orderConfirmationNotification = new OrderConfirmationNotification(decoratedSender);
        this.paymentReceiptNotification = new PaymentReceiptNotification(decoratedSender);

        // views
        this.catalogView = new ProductCatalogView(productService, scanner);
        this.checkoutView = new CheckoutView(orderService, scanner);
        this.paymentView = new PaymentView(paymentService, scanner);
        this.trackingView = new OrderTrackingView(shippingService, scanner);
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
                    new AdminRoleHandler(catalogView).handle(scanner);
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
