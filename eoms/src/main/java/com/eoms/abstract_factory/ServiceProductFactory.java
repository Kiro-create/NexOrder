package com.eoms.abstract_factory;

public class ServiceProductFactory implements ProductTypeFactory {

    @Override
    public StockHandler createStockHandler() {
        return new ServiceStockHandler();
    }

    @Override
    public InvoicePolicy createInvoicePolicy() {
        return new ServiceInvoicePolicy();
    }
}