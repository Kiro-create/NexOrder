package com.eoms.entity;

import com.eoms.flyweight.ProductFlyweight;
import com.eoms.flyweight.ProductFlyweightFactory;

public class Product {

    private int productId;
    private String name;
    private double price;
    private int stock;
    private ProductFlyweight flyweight;

    private Product(int productId, String name, double price, int stock, ProductFlyweight flyweight) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.flyweight = flyweight;
    }
    
    public enum ProductType {
        PHYSICAL, DIGITAL, SERVICE
    }

    public static Product createProduct(int id, String name, double price, int stock, ProductType type) {
        // Flyweight caches only intrinsic/shared state (ProductType).
        ProductFlyweight flyweight = ProductFlyweightFactory.getFlyweight(type);
        Product p = new Product(id, name, price, stock, flyweight);
        return p;
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
    
    public ProductType getType() {
        return flyweight.getType();
    }
}