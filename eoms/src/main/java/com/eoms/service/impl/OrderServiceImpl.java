package com.eoms.service.impl;

import com.eoms.service.OrderService;
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

        Logger logger = Logger.getInstance();

        int orderId = orderDAO.getNextOrderId();
        logger.info("Creating order with ID: " + orderId);
        return new Order(orderId, customer);
    }

    @Override
    public boolean addProductToOrder(Order order, int productId, int quantity) {
        InputValidator.validateNotNull(order, "Order");
        InputValidator.validatePositiveInt(productId, "Product ID");
        InputValidator.validateQuantity(quantity);

        Logger logger = Logger.getInstance();
        logger.info("Adding product to order. Product ID: " + productId);
        if (quantity <= 0) {
            logger.error("Invalid quantity for product: " + productId);
            return false;
        }

        Product product = productDAO.findProductById(productId);
        if (product == null) {
            logger.error("Product not found: " + productId);
            return false;
        }
        boolean available = InventoryManager.getInstance().reserveStock(product, quantity);
        if (!available) {
            logger.error("Not enough stock for product: " + productId);
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

        logger.log("Product added. Quantity: " + quantity + "; item total: " + itemTotal);
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

        Logger logger = Logger.getInstance();
        logger.info("Finalizing order ID: " + order.getOrderId());

        Order existingOrder = orderDAO.findOrderById(order.getOrderId());
        if (existingOrder != null) {
            logger.error("Order already created with ID: " + order.getOrderId());
            throw new IllegalArgumentException("Order already created with ID: " + order.getOrderId());
        }

        orderDAO.saveOrder(order);
        order.markFinalized();
        double total = order.getTotal();
        logger.log("Order saved. Total = " + total);
        return total;
    }
}
