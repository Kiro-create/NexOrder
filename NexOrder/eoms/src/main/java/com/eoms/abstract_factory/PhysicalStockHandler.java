package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public class PhysicalStockHandler extends AbstractStockHandler {

    @Override
    public void handleStock(Product product, int quantity) {
        validateQuantity(quantity);
        if (!product.decreaseStock(quantity)) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }
        System.out.println("Reducing warehouse stock for " + product.getName());
    }
}