package com.eoms.app;

import com.eoms.abstract_factory.ui.Dashboard;
import com.eoms.abstract_factory.ui.Menu;
import com.eoms.abstract_factory.ui.UserRole;
import com.eoms.bridge_notification.Notification;
import com.eoms.config.UIFactoryRegistry;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;
import com.eoms.factory.PaymentProcessor;
import com.eoms.service.OrderService;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.Boundary.CheckoutView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.OrderTrackingView;

import java.util.Scanner;
import com.eoms.util.InputValidator;

/**
 * Encapsulates the complete customer interaction loop.  Business logic is
 * delegated to service layer classes; Main no longer needs to know the
 * details of invoice fees or which processor to use (SRP, DIP).
 */
public class CustomerRoleHandler implements RoleHandler {
    private final ProductCatalogView catalogView;
    private final CheckoutView checkoutView;
    private final PaymentView paymentView;
    private final OrderTrackingView trackingView;
    private final OrderService orderService;
    private final Notification orderConfirmationNotification;
    private final Notification paymentReceiptNotification;
    private final PaymentProcessorProvider paymentProcessorProvider;

    public CustomerRoleHandler(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            OrderService orderService,
            Notification orderConfirmationNotification,
            Notification paymentReceiptNotification,
            PaymentProcessorProvider paymentProcessorProvider) {
        this.catalogView = catalogView;
        this.checkoutView = checkoutView;
        this.paymentView = paymentView;
        this.trackingView = trackingView;
        this.orderService = orderService;
        this.orderConfirmationNotification = orderConfirmationNotification;
        this.paymentReceiptNotification = paymentReceiptNotification;
        if (paymentProcessorProvider == null) {
            throw new IllegalArgumentException("paymentProcessorProvider must not be null");
        }
        this.paymentProcessorProvider = paymentProcessorProvider;
    }

    @Override
    public void handle(Scanner scanner) {
        Dashboard dashboard = UIFactoryRegistry.getUIFactory(UserRole.CUSTOMER).createDashboard();
        Menu menu = UIFactoryRegistry.getUIFactory(UserRole.CUSTOMER).createMenu();
        dashboard.showDashboard();

        boolean customerRunning = true;
        Customer customer = new Customer(1, "Customer", "customer@email.com");
        Order order = null;

        while (customerRunning) {
            menu.showMenu();
            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                InputValidator.validateRange(choice, 0, 6, "Menu choice");
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    catalogView.displayProducts();
                    break;
                case 2: // create order
                    if (order != null && !order.isFinalized()) {
                        System.out.println("Finish or pay the current order before creating a new one.");
                        break;
                    }
                    Order newOrder = checkoutView.createOrder(customer);
                    if (newOrder != null) {
                        order = newOrder;
                    }
                    break;
                case 3: // add product to order
                    if (order != null) {
                        if (order.isFinalized()) {
                            System.out.println("The current order is already complete. Create a new order to continue.");
                        } else {
                            checkoutView.addProductToOrder(order);
                        }
                    } else {
                        System.out.println("Create an order first.");
                    }
                    break;
                case 4: // finalize
                    if (order != null && !order.getItems().isEmpty()) {
                        double total = orderService.finalizeOrder(order);
                        System.out.println("Order finalized. Total: " + total);
                        orderConfirmationNotification.send("Order ID " + order.getOrderId() + " total = " + total);
                    } else {
                        System.out.println("Create an order and add items first.");
                    }
                    break;
                case 5: // payment
                    if (order != null && !order.getItems().isEmpty()) {
                        if (order.getStatus() != null && order.getStatus().contains("Paid")) {
                            System.out.println("Order already paid.");
                            break;
                        }
                        boolean paid = false;
                        while (!paid) {
                            System.out.println("Choose payment method:");
                            System.out.println("1. Credit Card");
                            System.out.println("2. PayPal");
                            System.out.println("3. Cash On Delivery");
                            System.out.println("0. Cancel");

                            int paymentChoice = scanner.nextInt();
                            InputValidator.validateRange(paymentChoice, 0, 3, "Payment choice");
                            scanner.nextLine();

                            if (paymentChoice == 0) {
                                break;
                            }

                            PaymentProcessor processor = paymentProcessorProvider.getProcessor(paymentChoice);
                            if (processor == null) {
                                System.out.println("Invalid payment method.");
                                continue;
                            }

                            // delegate ID prompt and service call to view
                            Payment payment = paymentView.makePayment(order, processor);

                            if ("Approved".equals(payment.getStatus())) {
                                paymentReceiptNotification.send(
                                        "Payment ID " + payment.getPaymentId() + " approved for order ID " + order.getOrderId());
                                paid = true;
                            } else {
                                // payment declined (e.g., exceeded COD cap). Let the user try another method.
                                System.out.println("Payment failed. Please choose another payment method.");
                            }
                        }
                    } else {
                        System.out.println("Create an order first.");
                    }
                    break;
                case 6:
                    trackingView.trackOrder();
                    break;
                case 0:
                    customerRunning = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

}
