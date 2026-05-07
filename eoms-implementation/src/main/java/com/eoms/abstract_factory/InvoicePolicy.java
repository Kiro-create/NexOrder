package com.eoms.abstract_factory;

import com.eoms.entity.Product;

public interface InvoicePolicy {
    double calculateExtraFees(Product product);
}