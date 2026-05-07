package com.eoms.Boundary;
import java.util.Scanner;

import com.eoms.app.mediator.CustomerMediator;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.util.InputValidator;

public class CheckoutView {

    private CustomerMediator mediator;
    private Scanner scanner;

    public CheckoutView(CustomerMediator mediator, Scanner scanner) {
        this.mediator = mediator;
        this.scanner = scanner;
    }

    public Order createOrder(Customer customer) {
        try {
            InputValidator.validateNotNull(customer, "Customer");

            Order order = mediator.createOrder(customer);
            System.out.println("Order created. Add products to cart using 'Add Product to Cart' option.");
            System.out.println("Your Order ID is " + order.getOrderId() + ".");

            return order;
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public void addProductToOrder(Order order) {
        try {
            InputValidator.validateNotNull(order, "Order");

            System.out.print("Product ID: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid product ID. Please enter a number.");
                scanner.next();   // clear the invalid input
                return;
            }

            int productId = scanner.nextInt();
            InputValidator.validatePositiveInt(productId, "Product ID");
            scanner.nextLine();

            System.out.print("Quantity: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid quantity. Please enter a number.");
                scanner.next();
                return;
            }

            int quantity = scanner.nextInt();
            InputValidator.validateQuantity(quantity);
            scanner.nextLine();

            boolean success = mediator.addProductToOrder(order, productId, quantity);

            if (success)
                System.out.println("Item added to order.");
            else
                System.out.println("Item could not be added (product not found or insufficient stock).");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void finalizeOrder(Order order) {

        double total = mediator.finalizeOrder(order);

        System.out.println("Order finalized. Total = " + total);
    }
}