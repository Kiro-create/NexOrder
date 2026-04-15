package com.eoms.app.mediator;

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
}
