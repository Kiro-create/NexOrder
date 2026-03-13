package com.eoms.Boundary;
<<<<<<< HEAD
import com.eoms.entity.Product;
import com.eoms.config.Logger;
import java.util.Scanner;

import com.eoms.service.OrderService;
=======

import java.util.Scanner;

import com.eoms.Controller.PlaceOrderController;
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
import com.eoms.entity.Customer;
import com.eoms.entity.Order;

public class CheckoutView {

<<<<<<< HEAD
    private OrderService orderService;
    private Scanner scanner;

    public CheckoutView(OrderService orderService, Scanner scanner) {
        this.orderService = orderService;
=======
    private PlaceOrderController controller;
    private Scanner scanner;

    public CheckoutView(PlaceOrderController controller, Scanner scanner) {
        this.controller = controller;
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
        this.scanner = scanner;
    }

    public Order createOrder(Customer customer) {
<<<<<<< HEAD
        Logger logger = Logger.getInstance();
=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

<<<<<<< HEAD
        Order order = new Order(orderId, customer);

        logger.info("Creating empty order with ID: " + orderId);
        System.out.println("Order created. Add products to cart using 'Add Product to Cart' option.");
=======
        Order order = controller.createOrder(orderId, customer);

        System.out.println("Order created.");
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

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

<<<<<<< HEAD
        boolean success = orderService.addProductToOrder(order, productId, quantity);
=======
        boolean success = controller.addProductToOrder(order, productId, quantity);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        if (success)
            System.out.println("Item added to order.");
        else
            System.out.println("Product not found.");
    }

    public void finalizeOrder(Order order) {

<<<<<<< HEAD
        double total = orderService.finalizeOrder(order);
=======
        double total = controller.finalizeOrder(order);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        System.out.println("Order finalized. Total = " + total);
    }
}