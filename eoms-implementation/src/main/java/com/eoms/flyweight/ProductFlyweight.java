package com.eoms.flyweight;

import com.eoms.config.Logger;
import com.eoms.entity.Product.ProductType;

public class ProductFlyweight {
    private ProductType type;

    public ProductFlyweight(ProductType type) {
        this.type = type;
    }

    // display() includes extrinsic instance data (productId, name, price, stock)
    // so only intrinsic/shared state is held by the flyweight itself.
    public void display(int productId, String name, double price, int stock) {
        Logger.getInstance().info(
                "Displaying product: " + name + " (" + type + ") id=" + productId + ", price=" + price + ", stock=" + stock);
        System.out.println(
                "Product ID: " + productId + ", Name: " + name + ", Type: " + type + ", Price: " + price + ", Stock: " + stock);
    }

    public ProductType getType() {
        return type;
    }
}