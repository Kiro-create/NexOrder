error id: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/entity/Product.java:com/eoms/flyweight/ProductFlyweightFactory#
file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/entity/Product.java
empty definition using pc, found symbol in pc: com/eoms/flyweight/ProductFlyweightFactory#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 99
uri: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/entity/Product.java
text:
```scala
package com.eoms.entity;

import com.eoms.flyweight.ProductFlyweight;
import com.eoms.flyweight.@@ProductFlyweightFactory;

public class Product {

    private int productId;
    private double price;
    private int stock;
    private ProductFlyweight flyweight;

    private Product(int productId, double price, int stock, ProductFlyweight flyweight) {
        this.productId = productId;
        this.price = price;
        this.stock = stock;
        this.flyweight = flyweight;
    }
    
    public enum ProductType {
        PHYSICAL, DIGITAL, SERVICE
    }

    public static Product createProduct(int id, String name, double price, int stock, ProductType type) {
        ProductFlyweight flyweight = ProductFlyweightFactory.getFlyweight(name, type);
        Product p = new Product(id, price, stock, flyweight);
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

    public String getName() { return flyweight.getName(); }

    public double getPrice() { return price; }

    public int getStock() { return stock; }
    
    public ProductType getType() {
        return flyweight.getType();
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/eoms/flyweight/ProductFlyweightFactory#