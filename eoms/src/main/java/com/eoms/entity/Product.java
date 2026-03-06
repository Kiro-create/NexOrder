package com.eoms.entity;

public class Product {

    private int productId;
    private String name;
    private double price;
    private int stock;

    private Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Product createProduct(int id, String name, double price, int stock) {
        return new Product(id, name, price, stock);
    }
    public void increaseStock(int quantity) {
        stock += quantity;
    }

    public boolean decreaseStock(int quantity) {

        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }

        return false;
    }
    public int getProductId() { return productId; }

    public String getName() { return name; }

    public double getPrice() { return price; }

    public int getStock() { return stock; }
}