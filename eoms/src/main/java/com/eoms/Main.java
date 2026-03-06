package com.eoms;

import java.util.Scanner;

import com.eoms.DAO.*;
import com.eoms.Controller.*;
import com.eoms.Boundary.*;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.factory.*;   

public class Main {


public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    // DAO layer
    ProductInterface productDAO = new ProductDAO();
    OrderInterface orderDAO = new OrderDAO();
    PaymentInterface paymentDAO = new PaymentDAO();
    ShipmentInterface shipmentDAO = new ShipmentDAO();


    // Controllers
    ManageProductCatalogController catalogController =
            new ManageProductCatalogController(productDAO);

    PlaceOrderController orderController =
            new PlaceOrderController(orderDAO, productDAO);

    ProcessPaymentController paymentController =
            new ProcessPaymentController(paymentDAO);

    TrackOrderStatusController trackingController =
            new TrackOrderStatusController(orderDAO, shipmentDAO);

    // Boundary layer
    ProductCatalogView catalogView =
            new ProductCatalogView(catalogController, scanner);

    CheckoutView checkoutView =
            new CheckoutView(orderController, scanner);

    PaymentView paymentView =
            new PaymentView(paymentController, scanner);

    OrderTrackingView trackingView =
            new OrderTrackingView(trackingController, scanner);

    boolean running = true;

    while (running) {

        System.out.println("=== E-OMS Demo ===");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("0. Exit");

        int role;

        if (scanner.hasNextInt()) {
            role = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            continue;
        }

        if (role == 0) {
            running = false;
        }

        else if (role == 1) {

            boolean adminRunning = true;

            while (adminRunning) {

                System.out.println("ADMIN PANEL");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("0. Back");

                int adminChoice = scanner.nextInt();
                scanner.nextLine();

                switch (adminChoice) {

                    case 1:
                        catalogView.addProduct();
                        break;

                    case 2:
                        catalogView.displayProducts();
                        break;

                    case 0:
                        adminRunning = false;
                        break;

                    default:
                        System.out.println("Invalid option");
                }
            }
        }

        else if (role == 2) {

            boolean customerRunning = true;

            Customer customer = new Customer(1, "Customer", "customer@email.com");
            Order order = null;

            while (customerRunning) {

                System.out.println("CUSTOMER PANEL");
                System.out.println("1. Browse Products");
                System.out.println("2. Create Order");
                System.out.println("3. Add Product to Cart");
                System.out.println("4. Checkout");
                System.out.println("5. Pay");
                System.out.println("6. Track Order");
                System.out.println("0. Back");

                int choice;

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                    continue;
                }

                switch (choice) {

                    case 1:
                        catalogView.displayProducts();
                        break;

                    case 2:
                        order = checkoutView.createOrder(customer);
                        break;

                    case 3:
                        if (order != null)
                            checkoutView.addProductToOrder(order);
                        else
                            System.out.println("Create an order first.");
                        break;

                    case 4:
                        if (order != null)
                            checkoutView.finalizeOrder(order);
                        else
                            System.out.println("Create an order first.");
                        break;

                    case 5:

                        if (order != null) {
                        	
                        	 if(order.getStatus() != null && order.getStatus().contains("Paid")) {
                                 System.out.println("Order already paid.");
                                 break;
                             }

                            double amount = order.calculateTotal();

                            System.out.println("Choose payment method:");
                            System.out.println("1. Credit Card");
                            System.out.println("2. PayPal");
                            System.out.println("3. Cash On Delivery");

                            int paymentChoice = scanner.nextInt();

                           
                         // FACTORY METHOD
                            PaymentProcessor processor = null;

                            switch(paymentChoice) {

                                case 1:
                                    processor = new CreditCardProcessor();
                                    break;

                                case 2:
                                    processor = new PayPalProcessor();
                                    break;

                                case 3:
                                    processor = new CashOnDeliveryProcessor();
                                    break;

                                default:
                                    System.out.println("Invalid payment method.");
                                    break;
                            }

                            if(processor != null) {

                                processor.processOrder(amount);

                                order.setStatus(processor.getPaymentStatus());

                                paymentView.makePayment(order);
                            } 
                            else {
                                System.out.println("Create an order first.");
                            }

                            break;
                        }
                    case 6:
                        trackingView.trackOrder();
                        break;

                    case 0:
                        customerRunning = false;
                        break;

                    default:
                        System.out.println("Invalid option");
                }
            }
        }

        else {
            System.out.println("Invalid choice.");
        }
    }

    scanner.close();
}


}
