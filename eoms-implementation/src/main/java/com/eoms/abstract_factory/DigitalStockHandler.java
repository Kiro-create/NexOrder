package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public class DigitalStockHandler extends AbstractStockHandler {

    @Override
    public void handleStock(Product product, int quantity) {
        validateQuantity(quantity);
        System.out.println("No physical stock reduction needed for " + product.getName());
    }
}