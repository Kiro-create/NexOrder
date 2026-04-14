package com.eoms.app.mediator;

import com.eoms.Boundary.CheckoutView;
import com.eoms.Boundary.OrderTrackingView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.abstract_factory.ui.PaymentMethodSelector;
import com.eoms.abstract_factory.ui.CustomerPaymentMethodSelector;
import com.eoms.app.PaymentProcessorProvider;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;
import com.eoms.factory.PaymentProcessor;
import com.eoms.util.InputValidator;

import java.util.Scanner;

/**
 * Concrete mediator for coordinating customer interactions.
 * This class orchestrates all interactions between:
 * - Views (ProductCatalog, Checkout, Payment, OrderTracking)
 * - Services (via views)
 * - Payment processors
 * 
 * By centralizing coordination here, we reduce coupling between views
 * and make it easier to modify interaction flows without changing multiple classes.
 * 
 * Note: This mediator only coordinates business logic - UI/menu handling
 * is the responsibility of CustomerRoleHandler.
 */
public class CustomerMediatorImpl implements CustomerMediator {

    private final ProductCatalogView catalogView;
    private final CheckoutView checkoutView;
    private final PaymentView paymentView;
    private final OrderTrackingView trackingView;
    private final PaymentProcessorProvider paymentProcessorProvider;
    
    private Scanner scanner;
    private Customer customer;
    private Order currentOrder;

    public CustomerMediatorImpl(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            PaymentProcessorProvider paymentProcessorProvider) {
        this.catalogView = catalogView;
        this.checkoutView = checkoutView;
        this.paymentView = paymentView;
        this.trackingView = trackingView;
        if (paymentProcessorProvider == null) {
            throw new IllegalArgumentException("paymentProcessorProvider must not be null");
        }
        this.paymentProcessorProvider = paymentProcessorProvider;
        
        // Initialize customer and order state
        this.customer = new Customer(1, "Customer", "customer@email.com");
        this.currentOrder = null;
    }

    @Override
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void viewCatalog() {
        catalogView.displayProducts();
    }

    @Override
    public void createNewOrder() {
        // Check if there's an existing unfinalized order
        if (currentOrder != null && !currentOrder.isFinalized()) {
            System.out.println("Finish or pay the current order before creating a new one.");
            return;
        }

        // Coordinate with CheckoutView to create a new order
        Order newOrder = checkoutView.createOrder(customer);
        if (newOrder != null) {
            this.currentOrder = newOrder;
        }
    }

    @Override
    public void addProductToCurrentOrder() {
        // Validate that an order exists
        if (currentOrder == null) {
            System.out.println("Create an order first.");
            return;
        }

        // Check if order is already finalized
        if (currentOrder.isFinalized()) {
            System.out.println("The current order is already complete. Create a new order to continue.");
            return;
        }

        // Coordinate with CheckoutView to add product
        checkoutView.addProductToOrder(currentOrder);
    }

    @Override
    public void finalizeCurrentOrder() {
        // Validate order state before finalizing
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            System.out.println("Create an order and add items first.");
            return;
        }

        // Coordinate with CheckoutView to finalize
        checkoutView.finalizeOrder(currentOrder);
    }

    @Override
    public void processPaymentForOrder() {
        // Validate that an order exists with items
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            System.out.println("Create an order first.");
            return;
        }

        // Check if order is already paid
        if (currentOrder.getStatus() != null && currentOrder.getStatus().contains("Paid")) {
            System.out.println("Order already paid.");
            return;
        }

        // Get the payment method selector for customer interactions
        PaymentMethodSelector paymentSelector = new CustomerPaymentMethodSelector();

        // Present payment method options and coordinate payment processing
        boolean paid = false;
        while (!paid) {
            int paymentChoice = paymentSelector.selectPaymentMethod(scanner);
            
            // Validate the choice
            if (paymentChoice == -1) {
                continue; // Invalid input, try again
            }
            
            if (paymentChoice == 0) {
                break; // User cancelled
            }

            // Validate range
            try {
                InputValidator.validateRange(paymentChoice, 0, 3, "Payment choice");
            } catch (IllegalArgumentException e) {
                System.out.println("Validation error: " + e.getMessage());
                continue;
            }

            // Get the appropriate payment processor from the provider
            PaymentProcessor processor = paymentProcessorProvider.getProcessor(paymentChoice);
            if (processor == null) {
                System.out.println("Invalid payment method.");
                continue;
            }

            // Coordinate with PaymentView to make the payment
            Payment payment = paymentView.makePayment(currentOrder, processor);

            // Check payment status and conclude if successful
            if ("Approved".equals(payment.getStatus())) {
                paid = true;
            } else {
                System.out.println("Payment failed. Please choose another payment method.");
            }
        }
    }

    @Override
    public void trackCurrentOrder() {
        if (currentOrder == null) {
            System.out.println("Create an order first before tracking.");
            return;
        }
        trackingView.trackOrder();
    }
}
