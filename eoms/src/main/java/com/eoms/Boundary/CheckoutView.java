package com.eoms.Boundary;
import com.eoms.entity.Product;
import com.eoms.config.Logger;
import java.util.Scanner;

import com.eoms.service.OrderService;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;

public class CheckoutView {

    private OrderService orderService;
    private Scanner scanner;

    public CheckoutView(OrderService orderService, Scanner scanner) {
        this.orderService = orderService;
        this.scanner = scanner;
    }

    public Order createOrder(Customer customer) {
        Logger logger = Logger.getInstance();

        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = new Order(orderId, customer);

        logger.info("Creating empty order with ID: " + orderId);
        System.out.println("Order created. Add products to cart using 'Add Product to Cart' option.");

        return order;
    }

    public void addProductToOrder(Order order) {

        System.out.print("Product ID: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid product ID. Please enter a number.");
            scanner.next();   // clear the invalid input
            return;
        }

        int productId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Quantity: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid quantity. Please enter a number.");
            scanner.next();
            return;
        }

        int quantity = scanner.nextInt();
        scanner.nextLine();

        boolean success = orderService.addProductToOrder(order, productId, quantity);

        if (success)
            System.out.println("Item added to order.");
        else
            System.out.println("Product not found.");
    }

    public void finalizeOrder(Order order) {

        double total = orderService.finalizeOrder(order);

        System.out.println("Order finalized. Total = " + total);
    }
}