package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public interface StockHandler {
    void handleStock(Product product, int quantity);
}