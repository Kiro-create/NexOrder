package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public class ServiceInvoicePolicy extends AbstractInvoicePolicy {

    @Override
    public double calculateExtraFees(Product product) {
        double fees = 25.0;
        logFees(fees);
        return fees;
    }
}