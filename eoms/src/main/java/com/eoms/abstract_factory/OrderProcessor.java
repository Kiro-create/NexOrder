package com.eoms.abstract_factory;

import com.eoms.entity.Customer;
import com.eoms.entity.Product;

public class OrderProcessor {

    private final StockHandler stockHandler;
    private final InvoicePolicy invoicePolicy;

    public OrderProcessor(ProductTypeFactory factory) {
        this.stockHandler = factory.createStockHandler();
        this.invoicePolicy = factory.createInvoicePolicy();
    }

    public double processOrder(Product product, Customer customer, int quantity) {
        stockHandler.handleStock(product, quantity);

        double subtotal = product.getPrice() * quantity;
        double extraFees = invoicePolicy.calculateExtraFees(product);
        double total = subtotal + extraFees;

        System.out.println("Customer: " + customer.getName());
        System.out.println("Subtotal: " + subtotal);
        System.out.println("Extra fees: " + extraFees);
        System.out.println("Final total: " + total);
        System.out.println();

        return total;
    }
}