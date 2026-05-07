package com.eoms.abstract_factory;

public abstract class AbstractInvoicePolicy implements InvoicePolicy {

    protected void logFees(double fees) {
        System.out.println("[Invoice] Extra fees = " + fees);
    }
}