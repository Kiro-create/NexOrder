package com.eoms.app.mediator;

import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;
import com.eoms.entity.Product;
import com.eoms.entity.Shipment;
import java.util.Scanner;

/**
 * Mediator interface for coordinating customer interactions.
 * Encapsulates communication between views, services, and other components
 * to reduce coupling and centralize business logic.
 */
public interface CustomerMediator {
    
    /**
     * Displays the product catalog to the customer.
     */
    void viewCatalog();
    
    /**
     * Initiates order creation for the customer.
     */
    void createNewOrder();
    
    /**
     * Adds a product to the current order.
     */
    void addProductToCurrentOrder();
    
    /**
     * Completes the order process using the order processing mediator.
     */
    void completeOrder();
    
    /**
     * Tracks the current order status.
     */
    void trackCurrentOrder();
    
    /**
     * Sets the scanner for input operations.
     * @param scanner Scanner for user input
     */
    void setScanner(Scanner scanner);
    
    /**
     * Creates a new order for the customer (called by Views).
     * @param customer The customer creating the order
     * @return The created order
     */
    Order createOrder(Customer customer);
    
    /**
     * Adds a product to an existing order (called by Views).
     * @param order The order to add to
     * @param productId The product ID
     * @param quantity The quantity
     * @return true if added successfully
     */
    boolean addProductToOrder(Order order, int productId, int quantity);
    
    /**
     * Finalizes an order (called by Views).
     * @param order The order to finalize
     * @return The total amount
     */
    double finalizeOrder(Order order);
    
    /**
     * Adds a new product to the catalog (called by Views).
     */
    boolean addProduct(int id, String name, double price, int stock, Product.ProductType type);
    
    /**
     * Processes a payment for an order (called by Views).
     */
    Payment processPayment(int paymentId, Order order, com.eoms.factory.PaymentProcessor processor);
    
    /**
     * Retrieves an order by ID (called by Views).
     */
    Order getOrderById(int orderId);
    
    /**
     * Retrieves shipment details for an order (called by Views).
     */
    Shipment getShipmentForOrder(int orderId);
    
    /**
     * Displays products in the catalog (called by Views).
     */
    List<Product> getProducts();
}
