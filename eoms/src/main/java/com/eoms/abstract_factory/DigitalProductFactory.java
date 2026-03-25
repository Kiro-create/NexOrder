package com.eoms.abstract_factory;

public class DigitalProductFactory implements ProductTypeFactory {

    @Override
    public StockHandler createStockHandler() {
        return new DigitalStockHandler();
    }

    @Override
    public InvoicePolicy createInvoicePolicy() {
        return new DigitalInvoicePolicy();
    }
}