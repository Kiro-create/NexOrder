package com.eoms.app.mediator;

import com.eoms.Boundary.CheckoutView;
import com.eoms.Boundary.OrderTrackingView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.abstract_factory.ui.PaymentMethodSelector;
import com.eoms.abstract_factory.ui.CustomerPaymentMethodSelector;
import com.eoms.app.PaymentProcessorProvider;
import com.eoms.app.mediator.OrderProcessingMediator;
import com.eoms.config.Logger;
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
    private PaymentProcessorProvider paymentProcessorProvider;
    private OrderProcessingMediator orderProcessingMediator;
    
    private Scanner scanner;
    private Customer customer;
    private Order currentOrder;

    public CustomerMediatorImpl(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            PaymentProcessorProvider paymentProcessorProvider) {
        this(catalogView, checkoutView, paymentView, trackingView, paymentProcessorProvider, null);
    }

    public CustomerMediatorImpl(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            PaymentProcessorProvider paymentProcessorProvider,
            OrderProcessingMediator orderProcessingMediator) {
        this.catalogView = catalogView;
        this.checkoutView = checkoutView;
        this.paymentView = paymentView;
        this.trackingView = trackingView;
        if (paymentProcessorProvider == null) {
            throw new IllegalArgumentException("paymentProcessorProvider must not be null");
        }
        this.paymentProcessorProvider = paymentProcessorProvider;
        this.orderProcessingMediator = orderProcessingMediator;
        
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
        } else {
            Logger.getInstance().error("CustomerMediator: Failed to create new order");
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
    public void completeOrder() {
        // Validate that an order exists with items
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            System.out.println("Create an order and add items first.");
            return;
        }

        // Check if order is already finalized
        if (currentOrder.isFinalized()) {
            System.out.println("Order already finalized.");
            return;
        }

        // Get the payment method selector for customer interactions
        PaymentMethodSelector paymentSelector = new CustomerPaymentMethodSelector();

        // Present payment method options and coordinate complete checkout
        boolean completed = false;
        while (!completed) {
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
                Logger.getInstance().error("CustomerMediator: No payment processor found for choice: " + paymentChoice);
                System.out.println("Invalid payment method.");
                continue;
            }

            // Use the OrderProcessingMediator to complete the entire workflow
            boolean success = orderProcessingMediator.processOrder(currentOrder, processor);

            if (success) {
                System.out.println("Checkout completed successfully! Your order has been processed and shipped.");
                completed = true;
            } else {
                System.out.println("Checkout failed. Please try again or choose another payment method.");
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
