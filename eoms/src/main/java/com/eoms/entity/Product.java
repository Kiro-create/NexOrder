package com.eoms.entity;

public class Product {

    private int productId;
    private String name;
    private double price;
    private int stock;
<<<<<<< HEAD
    private ProductType type;
=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

    private Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
<<<<<<< HEAD
    
    public enum ProductType {
        PHYSICAL, DIGITAL, SERVICE
    }

    public static Product createProduct(int id, String name, double price, int stock, ProductType type) {
        Product p = new Product(id, name, price, stock);
        p.setType(type);  // assign the type here
        return p;
=======

    public static Product createProduct(int id, String name, double price, int stock) {
        return new Product(id, name, price, stock);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
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
<<<<<<< HEAD
    
    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }
=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
}