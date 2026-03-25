package com.eoms.abstract_factory;

public class PhysicalProductFactory implements ProductTypeFactory {

    @Override
    public StockHandler createStockHandler() {
        return new PhysicalStockHandler();
    }

    @Override
    public InvoicePolicy createInvoicePolicy() {
        return new PhysicalInvoicePolicy();
    }
}