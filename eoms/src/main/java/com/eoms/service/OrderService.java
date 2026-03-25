package com.eoms.service;

import com.eoms.entity.Order;
import com.eoms.entity.Customer;
import com.eoms.entity.Product;

/**
 * Service abstraction for order processing logic.
 */
public interface OrderService {
    Order createOrder(int orderId, Customer customer);
    boolean addProductToOrder(Order order, int productId, int quantity);
    Product selectProductFromCatalog();
    double finalizeOrder(Order order);
}
