package com.eoms.abstract_factory;

public abstract class AbstractStockHandler implements StockHandler {

    protected void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }
}