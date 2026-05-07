package com.eoms.abstract_factory;

public interface ProductTypeFactory {
    StockHandler createStockHandler();
    InvoicePolicy createInvoicePolicy();
}