package com.eoms.entity;

import java.util.*;

public class Order {

    private int orderId;
    private Customer customer;
    private List<OrderItem> items;
    private String status;
<<<<<<< HEAD
    private Product product;
    private int quantity;
    private double total;
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

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

<<<<<<< HEAD
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
}