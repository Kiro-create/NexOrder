package com.eoms.flyweight;

import com.eoms.config.Logger;
import com.eoms.entity.Product.ProductType;

public class ConcreteProductFlyweight implements ProductFlyweight {
    private ProductType type;

    public ConcreteProductFlyweight(ProductType type) {
        this.type = type;
    }

    @Override
    public void display(int productId, String name, double price, int stock) {
        Logger.getInstance().info("Displaying product: " + name + " (" + type + ") id=" + productId + ", price=" + price + ", stock=" + stock);
        System.out.println("Product ID: " + productId + ", Name: " + name + ", Type: " + type + ", Price: " + price + ", Stock: " + stock);
    }

    @Override
    public ProductType getType() {
        return type;
    }
}