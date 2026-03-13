package com.eoms.Controller;

import com.eoms.DAO.OrderInterface;
import com.eoms.DAO.ProductInterface;
import com.eoms.entity.Order;
import com.eoms.entity.Product;
import com.eoms.entity.Customer;
import com.eoms.config.Logger;
import com.eoms.inventory.InventoryManager;

public class PlaceOrderController {

    private OrderInterface orderDAO;
    private ProductInterface productDAO;

    public PlaceOrderController(OrderInterface orderDAO, ProductInterface productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    public Order createOrder(int orderId, Customer customer) {

        Logger logger = Logger.getInstance();

        logger.info("Creating order with ID: " + orderId);

        return new Order(orderId, customer);
    }

    public boolean addProductToOrder(Order order, int productId, int quantity) {

        Logger logger = Logger.getInstance();

        logger.info("Adding product to order. Product ID: " + productId);

        Product product = productDAO.findProductById(productId);

        if (product == null) {
            logger.error("Product not found: " + productId);
            return false;
        }

        InventoryManager inventory = InventoryManager.getInstance();

        boolean available = inventory.reserveStock(product, quantity);

        if (!available) {
            logger.error("Not enough stock for product: " + productId);
            return false;
        }

        order.addProduct(product, quantity);

        logger.log("Product added. Quantity: " + quantity);

        return true;
    }

    public double finalizeOrder(Order order) {

        Logger logger = Logger.getInstance();

        logger.info("Finalizing order ID: " + order.getOrderId());

        orderDAO.saveOrder(order);

        double total = order.calculateTotal();

        logger.log("Order saved. Total = " + total);

        return total;
    }

}