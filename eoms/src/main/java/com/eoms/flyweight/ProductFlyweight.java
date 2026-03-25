package com.eoms.flyweight;

import com.eoms.entity.Product.ProductType;

public interface ProductFlyweight {
    // display() includes extrinsic instance data (productId, name, price, stock)
    // so only intrinsic/shared state is held by the flyweight itself.
    void display(int productId, String name, double price, int stock);
    ProductType getType();
}