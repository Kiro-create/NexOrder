package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public class DigitalInvoicePolicy extends AbstractInvoicePolicy {

    @Override
    public double calculateExtraFees(Product product) {
        double fees = 0.0;
        logFees(fees);
        return fees;
    }
}