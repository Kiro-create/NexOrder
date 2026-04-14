package com.eoms.app.mediator;

import com.eoms.Boundary.CheckoutView;
import com.eoms.Boundary.OrderTrackingView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.ProductCatalogView;
import com.eoms.abstract_factory.ui.PaymentMethodSelector;
import com.eoms.abstract_factory.ui.CustomerPaymentMethodSelector;
import com.eoms.app.PaymentProcessorProvider;
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
        Logger.getInstance().info("CustomerMediator: Scanner set for input operations");
    }

    @Override
    public void viewCatalog() {
        Logger.getInstance().info("CustomerMediator: Displaying product catalog");
        catalogView.displayProducts();
        Logger.getInstance().info("CustomerMediator: Product catalog displayed successfully");
    }

    @Override
    public void createNewOrder() {
        Logger.getInstance().info("CustomerMediator: Attempting to create new order for customer: " + customer.getName());
        
        // Check if there's an existing unfinalized order
        if (currentOrder != null && !currentOrder.isFinalized()) {
            Logger.getInstance().log("CustomerMediator: Cannot create new order - existing unfinalized order present");
            System.out.println("Finish or pay the current order before creating a new one.");
            return;
        }

        // Coordinate with CheckoutView to create a new order
        Logger.getInstance().info("CustomerMediator: Coordinating with CheckoutView to create order");
        Order newOrder = checkoutView.createOrder(customer);
        if (newOrder != null) {
            this.currentOrder = newOrder;
            Logger.getInstance().info("CustomerMediator: New order created successfully with ID: " + newOrder.getOrderId());
        } else {
            Logger.getInstance().error("CustomerMediator: Failed to create new order");
        }
    }

    @Override
    public void addProductToCurrentOrder() {
        Logger.getInstance().info("CustomerMediator: Attempting to add product to current order");
        
        // Validate that an order exists
        if (currentOrder == null) {
            Logger.getInstance().log("CustomerMediator: No current order exists for adding products");
            System.out.println("Create an order first.");
            return;
        }

        // Check if order is already finalized
        if (currentOrder.isFinalized()) {
            Logger.getInstance().log("CustomerMediator: Cannot add products to finalized order ID: " + currentOrder.getOrderId());
            System.out.println("The current order is already complete. Create a new order to continue.");
            return;
        }

        // Coordinate with CheckoutView to add product
        Logger.getInstance().info("CustomerMediator: Coordinating with CheckoutView to add product to order ID: " + currentOrder.getOrderId());
        checkoutView.addProductToOrder(currentOrder);
        Logger.getInstance().info("CustomerMediator: Product addition process completed for order ID: " + currentOrder.getOrderId());
    }

    @Override
    public void finalizeCurrentOrder() {
        Logger.getInstance().info("CustomerMediator: Attempting to finalize current order");
        
        // Validate order state before finalizing
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            Logger.getInstance().log("CustomerMediator: Cannot finalize - no valid order exists");
            System.out.println("Create an order and add items first.");
            return;
        }

        Logger.getInstance().info("CustomerMediator: Coordinating with CheckoutView to finalize order ID: " + currentOrder.getOrderId());
        // Coordinate with CheckoutView to finalize
        checkoutView.finalizeOrder(currentOrder);
        Logger.getInstance().info("CustomerMediator: Order finalization completed for order ID: " + currentOrder.getOrderId());
    }

    @Override
    public void processPaymentForOrder() {
        Logger.getInstance().info("CustomerMediator: Starting payment processing for current order");
        
        // Validate that an order exists with items
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            Logger.getInstance().log("CustomerMediator: Cannot process payment - no valid order exists");
            System.out.println("Create an order first.");
            return;
        }

        // Check if order is already paid
        if (currentOrder.getStatus() != null && currentOrder.getStatus().contains("Paid")) {
            Logger.getInstance().log("CustomerMediator: Order already paid - order ID: " + currentOrder.getOrderId());
            System.out.println("Order already paid.");
            return;
        }

        // Get the payment method selector for customer interactions
        Logger.getInstance().info("CustomerMediator: Presenting payment method selection");
        PaymentMethodSelector paymentSelector = new CustomerPaymentMethodSelector();

        // Present payment method options and coordinate payment processing
        boolean paid = false;
        while (!paid) {
            int paymentChoice = paymentSelector.selectPaymentMethod(scanner);
            
            // Validate the choice
            if (paymentChoice == -1) {
                Logger.getInstance().info("CustomerMediator: Invalid payment method input, retrying");
                continue; // Invalid input, try again
            }
            
            if (paymentChoice == 0) {
                Logger.getInstance().info("CustomerMediator: User cancelled payment process");
                break; // User cancelled
            }

            // Validate range
            try {
                InputValidator.validateRange(paymentChoice, 0, 3, "Payment choice");
            } catch (IllegalArgumentException e) {
                Logger.getInstance().log("CustomerMediator: Payment choice validation failed: " + e.getMessage());
                System.out.println("Validation error: " + e.getMessage());
                continue;
            }

            // Get the appropriate payment processor from the provider
            Logger.getInstance().info("CustomerMediator: Retrieving payment processor for choice: " + paymentChoice);
            PaymentProcessor processor = paymentProcessorProvider.getProcessor(paymentChoice);
            if (processor == null) {
                Logger.getInstance().error("CustomerMediator: No payment processor found for choice: " + paymentChoice);
                System.out.println("Invalid payment method.");
                continue;
            }

            // Coordinate with PaymentView to make the payment
            Logger.getInstance().info("CustomerMediator: Coordinating payment with PaymentView for order ID: " + currentOrder.getOrderId());
            Payment payment = paymentView.makePayment(currentOrder, processor);

            // Check payment status and conclude if successful
            if ("Approved".equals(payment.getStatus())) {
                Logger.getInstance().info("CustomerMediator: Payment approved successfully for order ID: " + currentOrder.getOrderId());
                paid = true;
            } else {
                Logger.getInstance().log("CustomerMediator: Payment failed for order ID: " + currentOrder.getOrderId() + ", status: " + payment.getStatus());
                System.out.println("Payment failed. Please choose another payment method.");
            }
        }
        
        if (paid) {
            Logger.getInstance().info("CustomerMediator: Payment process completed successfully");
        } else {
            Logger.getInstance().info("CustomerMediator: Payment process cancelled or failed");
        }
    }

    @Override
    public void trackCurrentOrder() {
        Logger.getInstance().info("CustomerMediator: Attempting to track current order");
        
        if (currentOrder == null) {
            Logger.getInstance().log("CustomerMediator: No current order to track");
            System.out.println("Create an order first before tracking.");
            return;
        }
        
        Logger.getInstance().info("CustomerMediator: Coordinating with OrderTrackingView for order ID: " + currentOrder.getOrderId());
        trackingView.trackOrder();
        Logger.getInstance().info("CustomerMediator: Order tracking completed for order ID: " + currentOrder.getOrderId());
    }
}
