error id: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/eoms/eoms/src/main/java/com/eoms/Main.java:com/eoms/abstract_factory/InvoicePolicy#
file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/eoms/eoms/src/main/java/com/eoms/Main.java
empty definition using pc, found symbol in pc: com/eoms/abstract_factory/InvoicePolicy#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 189
uri: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/eoms/eoms/src/main/java/com/eoms/Main.java
text:
```scala
package com.eoms;
import com.eoms.abstract_factory.ProductTypeFactory;
import com.eoms.entity.Product;
import com.eoms.abstract_factory.OrderProcessor;
import com.eoms.abstract_factory.@@InvoicePolicy;
import com.eoms.abstract_factory.PhysicalProductFactory;
import com.eoms.abstract_factory.DigitalProductFactory;
import com.eoms.abstract_factory.ServiceProductFactory;
import com.eoms.abstract_factory.ui.AdminUIFactory;
import com.eoms.abstract_factory.ui.CustomerUIFactory;
import com.eoms.abstract_factory.ui.UIFactory;
import com.eoms.abstract_factory.ui.UserRole;
import com.eoms.abstract_factory.ui.Menu;
import com.eoms.abstract_factory.ui.Dashboard;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;

import com.eoms.DAO.*;
import com.eoms.Controller.*;
import com.eoms.Boundary.*;
import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.factory.*;   

public class Main {

private static final Map<String, Supplier<UIFactory>> UI_FACTORY_REGISTRY = new LinkedHashMap<>();

static {
    registerUIFactory("ADMIN", AdminUIFactory::new);
    registerUIFactory("CUSTOMER", CustomerUIFactory::new);
}

public static synchronized void registerUIFactory(String role, Supplier<UIFactory> factorySupplier) {
    if (role == null || role.trim().isEmpty()) {
        throw new IllegalArgumentException("Role must not be null or empty.");
    }
    if (factorySupplier == null) {
        throw new IllegalArgumentException("Factory supplier must not be null.");
    }

    UI_FACTORY_REGISTRY.put(normalizeRole(role), factorySupplier);
}

public static UIFactory getUIFactory(UserRole role) {
    if (role == null) {
        throw new IllegalArgumentException("Role must not be null.");
    }
    return getUIFactory(role.name());
}

public static synchronized UIFactory getUIFactory(String role) {
    if (role == null || role.trim().isEmpty()) {
        throw new IllegalArgumentException("Role must not be null or empty.");
    }

    String normalizedRole = normalizeRole(role);
    Supplier<UIFactory> factorySupplier = UI_FACTORY_REGISTRY.get(normalizedRole);

    if (factorySupplier == null) {
        throw new IllegalArgumentException(
                "Unsupported role: " + role + ". Supported roles: " + supportedUIRoles());
    }

    return factorySupplier.get();
}

public static synchronized Set<String> supportedUIRoles() {
    return Set.copyOf(UI_FACTORY_REGISTRY.keySet());
}

private static String normalizeRole(String role) {
    return role.trim().toUpperCase(Locale.ROOT);
}


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

            UIFactory uiFactory = getUIFactory(UserRole.ADMIN);
            Menu menu = uiFactory.createMenu();
            Dashboard dashboard = uiFactory.createDashboard();

            dashboard.showDashboard();

            boolean adminRunning = true;

            while (adminRunning) {

                menu.showMenu();

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

            UIFactory uiFactory = getUIFactory(UserRole.CUSTOMER);
            Menu menu = uiFactory.createMenu();
            Dashboard dashboard = uiFactory.createDashboard();

            dashboard.showDashboard();

            boolean customerRunning = true;

            Customer customer = new Customer(1, "Customer", "customer@email.com");
            Order order = null;
            OrderProcessor orderProcessor = null;  // declare before switch


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

                    case 2: // CREATE ORDER
                        order = checkoutView.createOrder(customer);
                        orderProcessor = null;  // reset processor for new order
                        break;

                    case 3: // Add Product to Cart
                        if (order != null) {
                            checkoutView.addProductToOrder(order);
                            
                            // Now calculate the item total (stock already reserved by controller)
                            List<com.eoms.entity.OrderItem> items = order.getItems();
                            if (!items.isEmpty()) {
                                com.eoms.entity.OrderItem lastItem = items.get(items.size() - 1);
                                Product product = lastItem.getProduct();
                                int quantity = lastItem.getQuantity();
                                
                                // Select the right factory to get invoice policy (without stock operations)
                                ProductTypeFactory factory = null;
                                switch (product.getType()) {
                                    case PHYSICAL:
                                        factory = new PhysicalProductFactory();
                                        break;
                                    case DIGITAL:
                                        factory = new DigitalProductFactory();
                                        break;
                                    case SERVICE:
                                        factory = new ServiceProductFactory();
                                        break;
                                }
                                
                                if (factory != null) {
                                    // Get invoice policy to calculate fees
                                    InvoicePolicy invoicePolicy = factory.createInvoicePolicy();
                                    double subtotal = product.getPrice() * quantity;
                                    double extraFees = invoicePolicy.calculateExtraFees(product);
                                    double itemTotal = subtotal + extraFees;
                                    
                                    double currentTotal = order.getTotal() == 0 ? itemTotal : order.getTotal() + itemTotal;
                                    order.setTotal(currentTotal);
                                    
                                    System.out.println("Item added to cart.");
                                    System.out.println("Subtotal: " + subtotal + " | Fees: " + extraFees + " | Item Total: " + itemTotal);
                                    System.out.println("Order total: " + currentTotal);
                                }
                            }
                        } else {
                            System.out.println("Create an order first.");
                        }
                        break;

                    case 4: // Finalize Order
                        if (order != null && orderProcessor != null) {

                            double total = orderController.finalizeOrder(order); // <-- saves order

                            System.out.println("Order finalized. Total: " + total);

                        } else {
                            System.out.println("Create an order first.");
                        }
                        break;
                    case 5: // Payment
                        if (order != null && orderProcessor != null) {

                            if(order.getStatus() != null && order.getStatus().contains("Paid")) {
                                System.out.println("Order already paid.");
                                break;  // exits the switch safely
                            }

                            // proceed with payment
                            double amount = order.calculateTotal();

                            System.out.println("Choose payment method:");
                            System.out.println("1. Credit Card");
                            System.out.println("2. PayPal");
                            System.out.println("3. Cash On Delivery");

                            int paymentChoice = scanner.nextInt();

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
                            } else {
                                System.out.println("Invalid payment method.");
                            }

                        } else {
                            System.out.println("Create an order first.");
                        }

                        break; // break at the end of the case
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

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/eoms/abstract_factory/InvoicePolicy#