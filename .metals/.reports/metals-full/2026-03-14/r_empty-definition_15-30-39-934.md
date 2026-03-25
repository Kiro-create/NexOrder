error id: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/app/CustomerRoleHandler.java:com/eoms/Boundary/ProductCatalogView#
file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/app/CustomerRoleHandler.java
empty definition using pc, found symbol in pc: com/eoms/Boundary/ProductCatalogView#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 605
uri: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/app/CustomerRoleHandler.java
text:
```scala
package com.eoms.app;

import com.eoms.abstract_factory.ui.Dashboard;
import com.eoms.adapter.PaymentGatewayAdapter;
import com.eoms.abstract_factory.ui.Menu;
import com.eoms.abstract_factory.ui.UserRole;
import com.eoms.config.UIFactoryRegistry;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.factory.PaymentProcessor;
import com.eoms.factory.CashOnDeliveryProcessor;
import com.eoms.factory.CreditCardProcessor;
import com.eoms.factory.PayPalProcessor;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.Boundary.@@ProductCatalogView;
import com.eoms.Boundary.CheckoutView;
import com.eoms.Boundary.PaymentView;
import com.eoms.Boundary.OrderTrackingView;

import java.util.Scanner;

/**
 * Encapsulates the complete customer interaction loop.  Business logic is
 * delegated to service layer classes; Main no longer needs to know the
 * details of invoice fees or which processor to use (SRP, DIP).
 */
public class CustomerRoleHandler implements RoleHandler {
    private final ProductCatalogView catalogView;
    private final CheckoutView checkoutView;
    private final PaymentView paymentView;
    private final OrderTrackingView trackingView;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public CustomerRoleHandler(
            ProductCatalogView catalogView,
            CheckoutView checkoutView,
            PaymentView paymentView,
            OrderTrackingView trackingView,
            OrderService orderService,
            PaymentService paymentService) {
        this.catalogView = catalogView;
        this.checkoutView = checkoutView;
        this.paymentView = paymentView;
        this.trackingView = trackingView;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Override
    public void handle(Scanner scanner) {
        Dashboard dashboard = UIFactoryRegistry.getUIFactory(UserRole.CUSTOMER).createDashboard();
        Menu menu = UIFactoryRegistry.getUIFactory(UserRole.CUSTOMER).createMenu();
        dashboard.showDashboard();

        boolean customerRunning = true;
        Customer customer = new Customer(1, "Customer", "customer@email.com");
        Order order = null;

        while (customerRunning) {
            menu.showMenu();
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
                case 2: // create order
                    order = checkoutView.createOrder(customer);
                    break;
                case 3: // add product to order
                    if (order != null) {
                        checkoutView.addProductToOrder(order);
                    } else {
                        System.out.println("Create an order first.");
                    }
                    break;
                case 4: // finalize
                    if (order != null && !order.getItems().isEmpty()) {
                        double total = orderService.finalizeOrder(order);
                        System.out.println("Order finalized. Total: " + total);
                    } else {
                        System.out.println("Create an order and add items first.");
                    }
                    break;
                case 5: // payment
                    if (order != null && !order.getItems().isEmpty()) {
                        if (order.getStatus() != null && order.getStatus().contains("Paid")) {
                            System.out.println("Order already paid.");
                            break;
                        }
                        System.out.println("Choose payment method:");
                        System.out.println("1. Credit Card");
                        System.out.println("2. PayPal");
                        System.out.println("3. Cash On Delivery");
                        int paymentChoice = scanner.nextInt();
                        scanner.nextLine();
                        PaymentProcessor processor = null;
                        switch (paymentChoice) {
                        case 1:
                            processor = new PaymentGatewayAdapter(new CreditCardProcessor());
                            break;
                        case 2:
                            processor = new PaymentGatewayAdapter(new PayPalProcessor());
                            break;
                        case 3:
                            processor = new CashOnDeliveryProcessor();
                            break;
                    
                            default:
                                System.out.println("Invalid payment method.");
                        }
                        if (processor != null) {
                            // delegate ID prompt and service call to view
                            paymentView.makePayment(order, processor);
                        }
                    } else {
                        System.out.println("Create an order first.");
                    }
                    break;
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
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/eoms/Boundary/ProductCatalogView#