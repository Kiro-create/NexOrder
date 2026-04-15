package com.eoms.service.impl;

import com.eoms.service.OrderService;
import com.eoms.observer.OrderEvent;
import com.eoms.observer.OrderEventManager;
import com.eoms.observer.OrderEventType;
import com.eoms.DAO.OrderInterface;
import com.eoms.DAO.ProductInterface;
import com.eoms.entity.Order;
import com.eoms.entity.Product;
import com.eoms.entity.Customer;
import com.eoms.config.Logger;
import com.eoms.inventory.InventoryManager;
import com.eoms.util.InputValidator;

public class OrderServiceImpl implements OrderService {
    private final OrderInterface orderDAO;
    private final ProductInterface productDAO;

    public OrderServiceImpl(OrderInterface orderDAO, ProductInterface productDAO) {
        if (orderDAO == null || productDAO == null) {
            throw new IllegalArgumentException("DAOs must not be null");
        }
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Order createOrder(Customer customer) {
        InputValidator.validateNotNull(customer, "Customer");

        int orderId = orderDAO.getNextOrderId();
        Order order = new Order(orderId, customer);

        OrderEventManager.getInstance().notifyListeners(
            new OrderEvent(
                OrderEventType.ORDER_CREATED,
                order,
                "Your order has been created successfully."
            )
        );

        return order;
    }

    @Override
    public boolean addProductToOrder(Order order, int productId, int quantity) {
        InputValidator.validateNotNull(order, "Order");
        InputValidator.validatePositiveInt(productId, "Product ID");
        InputValidator.validateQuantity(quantity);

        if (quantity <= 0) {
            return false;
        }

        Product product = productDAO.findProductById(productId);
        if (product == null) {
            return false;
        }
        boolean available = InventoryManager.getInstance().reserveStock(product, quantity);
        if (!available) {
            return false;
        }

        // attach item and update totals (business rule moved from Main)
        order.addProduct(product, quantity);

        // calculate extra fees based on product type
        com.eoms.abstract_factory.ProductTypeFactory factory =
                com.eoms.app.ProductTypeFactoryProvider.getFactory(product.getType());
        com.eoms.abstract_factory.InvoicePolicy invoicePolicy = factory.createInvoicePolicy();
        double subtotal = product.getPrice() * quantity;
        double extraFees = invoicePolicy.calculateExtraFees(product);
        double itemTotal = subtotal + extraFees;

        double currentTotal = order.getTotal() == 0 ? itemTotal : order.getTotal() + itemTotal;
        order.setTotal(currentTotal);

        return true;
    }

    @Override
    public Product selectProductFromCatalog() {
        if (productDAO.getAllProducts().isEmpty()) {
            System.out.println("No products available.");
            return null;
        }
        return productDAO.getAllProducts().get(0);
    }

    @Override
    public double finalizeOrder(Order order) {
        InputValidator.validateNotNull(order, "Order");

        Order existingOrder = orderDAO.findOrderById(order.getOrderId());
        if (existingOrder != null) {
            throw new IllegalArgumentException("Order already created with ID: " + order.getOrderId());
        }

        orderDAO.saveOrder(order);
        order.markFinalized();

        OrderEventManager.getInstance().notifyListeners(
            new OrderEvent(
                OrderEventType.ORDER_FINALIZED,
                order,
                "Your order has been finalized successfully."
            )
        );

        double total = order.getTotal();
        return total;
    }
}
