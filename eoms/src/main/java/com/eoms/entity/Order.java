package com.eoms.entity;

import java.util.*;

public class Order {

    private int orderId;
    private Customer customer;
    private List<OrderItem> items;
    private String status;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = "Created";
    }

    public void addProduct(Product product, int quantity) {

        OrderItem item = new OrderItem(product, quantity);
        items.add(item);

    }

    public double calculateTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}