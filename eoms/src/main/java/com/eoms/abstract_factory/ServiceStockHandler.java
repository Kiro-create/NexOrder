package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public class ServiceStockHandler extends AbstractStockHandler {

    @Override
    public void handleStock(Product product, int quantity) {
        validateQuantity(quantity);
        System.out.println("Checking staff/time-slot availability for " + product.getName());
    }
}