package com.eoms.Boundary;
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
        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = orderService.createOrder(orderId, customer);
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
            System.out.println("Item could not be added (product not found or insufficient stock).");
    }

    public void finalizeOrder(Order order) {

        double total = orderService.finalizeOrder(order);

        System.out.println("Order finalized. Total = " + total);
    }
}