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
import com.eoms.entity.Product;
import com.eoms.factory.PaymentProcessor;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.service.ProductService;
import com.eoms.service.ShippingService;
import com.eoms.util.InputValidator;

import java.util.List;
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

    private ProductCatalogView catalogView;
    private CheckoutView checkoutView;
    private PaymentView paymentView;
    private OrderTrackingView trackingView;
    private final PaymentProcessorProvider paymentProcessorProvider;
    private final OrderProcessingMediator orderProcessingMediator;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final ShippingService shippingService;
    
    private Scanner scanner;
    private Customer customer;
    private Order currentOrder;

    public CustomerMediatorImpl(
            PaymentProcessorProvider paymentProcessorProvider,
            OrderProcessingMediator orderProcessingMediator,
            OrderService orderService,
            PaymentService paymentService,
            ProductService productService,
            ShippingService shippingService) {
        if (paymentProcessorProvider == null) {
            throw new IllegalArgumentException("paymentProcessorProvider must not be null");
        }
        this.paymentProcessorProvider = paymentProcessorProvider;
        this.orderProcessingMediator = orderProcessingMediator;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.productService = productService;
        this.shippingService = shippingService;
        
        // Initialize customer and order state
        this.customer = new Customer(1, "Customer", "customer@email.com");
        this.currentOrder = null;
    }

    public void setViews(ProductCatalogView catalogView, CheckoutView checkoutView, PaymentView paymentView, OrderTrackingView trackingView) {
        this.catalogView = catalogView;
        this.checkoutView = checkoutView;
        this.paymentView = paymentView;
        this.trackingView = trackingView;
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

            PaymentProcessor processor = paymentProcessorProvider.getProcessor(paymentChoice);
            if (processor == null) {
                Logger.getInstance().error("CustomerMediator: No payment processor found for choice: " + paymentChoice);
                System.out.println("Invalid payment method.");
                continue;
            }

        boolean success = orderProcessingMediator.processOrder(currentOrder, processor);

        if (success) {
            System.out.println("Checkout completed successfully!");
            completed = true;
        } else {
            System.out.println("Checkout failed. Try again.");
            if (currentOrder.isFinalized()) {
                Logger.getInstance().error("CustomerMediator: Order was finalized but processing failed later; cannot retry with same order. Order ID: " + currentOrder.getOrderId());
                System.out.println("This order is already finalized. Create a new order to try again.");
                break;
            }
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

    @Override
    public Order createOrder(Customer customer) {
        InputValidator.validateNotNull(customer, "Customer");
        return orderService.createOrder(customer);
    }

    @Override
    public boolean addProductToOrder(Order order, int productId, int quantity) {
        InputValidator.validateNotNull(order, "Order");
        InputValidator.validatePositiveInt(productId, "Product ID");
        InputValidator.validateQuantity(quantity);

        if (quantity <= 0) return false;

        return orderService.addProductToOrder(order, productId, quantity);
    }

    @Override
    public double finalizeOrder(Order order) {
        InputValidator.validateNotNull(order, "Order");
        return orderService.finalizeOrder(order);
    }

    @Override
    public boolean addProduct(int id, String name, double price, int stock, Product.ProductType type) {
        InputValidator.validatePositiveInt(id, "Product ID");
        InputValidator.validateNonEmptyString(name, "Product name");
        InputValidator.validateStringLength(name, InputValidator.MAX_STRING_LENGTH, "Product name");
        InputValidator.validatePrice(price);
        InputValidator.validateStock(stock);
        return productService.addProduct(id, name, price, stock, type);
    }

    @Override
    public com.eoms.entity.Payment processPayment(int paymentId, Order order, PaymentProcessor processor) {
        return paymentService.processPayment(paymentId, order, processor);
    }

    @Override
    public Order getOrderById(int orderId) {
        return shippingService.getOrder(orderId);
    }

    @Override
    public com.eoms.entity.Shipment getShipmentForOrder(int orderId) {
        return shippingService.getShipmentForOrder(orderId);
    }

    @Override
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }
}
