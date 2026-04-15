package com.eoms.app;

import com.eoms.app.mediator.CustomerMediator;
import com.eoms.app.mediator.CustomerMediatorImpl;
import com.eoms.abstract_factory.ui.Dashboard;
import com.eoms.abstract_factory.ui.Menu;
import com.eoms.abstract_factory.ui.UIFactory;
import com.eoms.abstract_factory.ui.UserRole;
import com.eoms.bridge_notification.Notification;
import com.eoms.config.UIFactoryRegistry;
import com.eoms.service.OrderService;
import com.eoms.service.OrderProcessingMediator;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.Boundary.CheckoutView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.OrderTrackingView;
import com.eoms.util.InputValidator;

import java.util.Scanner;

/**
 * Handles customer role interactions by managing the UI flow and delegating
 * business coordination to the mediator. This class is responsible for:
 * - UI presentation (dashboard, menu, input handling)
 * - User interaction flow
 * - Delegating business logic to the mediator
 */
public class CustomerRoleHandler implements RoleHandler {
    private final CustomerMediator customerMediator;

    public CustomerRoleHandler(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            OrderService orderService,
            Notification orderConfirmationNotification,
            Notification paymentReceiptNotification,
            PaymentProcessorProvider paymentProcessorProvider) {
        this(catalogView, checkoutView, paymentView, trackingView, orderService, orderConfirmationNotification, paymentReceiptNotification, paymentProcessorProvider, null);
    }

    public CustomerRoleHandler(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            OrderService orderService,
            Notification orderConfirmationNotification,
            Notification paymentReceiptNotification,
            PaymentProcessorProvider paymentProcessorProvider,
            OrderProcessingMediator orderProcessingMediator) {
        // Unused parameters kept for backward compatibility with EomsApplication
        // The mediator is now responsible for coordinating these components
        if (paymentProcessorProvider == null) {
            throw new IllegalArgumentException("paymentProcessorProvider must not be null");
        }
        
        // Create mediator for business coordination
        this.customerMediator = new CustomerMediatorImpl(
                catalogView,
                checkoutView,
                paymentView,
                trackingView,
                paymentProcessorProvider,
                orderProcessingMediator);
    }

    @Override
    public void handle(Scanner scanner) {
        // Set scanner for mediator's input operations
        customerMediator.setScanner(scanner);

        // Get UI components for customer role
        UIFactory uiFactory = UIFactoryRegistry.getUIFactory(UserRole.CUSTOMER);
        Dashboard dashboard = uiFactory.createDashboard();
        Menu menu = uiFactory.createMenu();

        // Display dashboard
        dashboard.showDashboard();

        // Handle user interaction loop
        boolean customerRunning = true;
        while (customerRunning) {
            menu.showMenu();

            int choice = 0;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                InputValidator.validateRange(choice, 0, 5, "Menu choice");
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            // Delegate business coordination to mediator
            switch (choice) {
                case 1:
                    customerMediator.viewCatalog();
                    break;
                case 2:
                    customerMediator.createNewOrder();
                    break;
                case 3:
                    customerMediator.addProductToCurrentOrder();
                    break;
                case 4:
                    customerMediator.completeOrder();
                    break;
                case 5:
                    customerMediator.trackCurrentOrder();
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
