package com.eoms.app;

import com.eoms.abstract_factory.ProductTypeFactory;
import com.eoms.abstract_factory.DigitalProductFactory;
import com.eoms.abstract_factory.PhysicalProductFactory;
import com.eoms.abstract_factory.ServiceProductFactory;
import com.eoms.entity.Product;

/**
 * Simple helper that returns the correct factory for a given product type.
 * Factored out of business logic so the service layer can remain clean and
 * easily extensible (Open/Closed).  Adding another product type requires
 * updating only this class.
 */
public final class ProductTypeFactoryProvider {
    private ProductTypeFactoryProvider() {}

    public static ProductTypeFactory getFactory(Product.ProductType type) {
        if (type == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }
        switch (type) {
            case PHYSICAL:
                return new PhysicalProductFactory();
            case DIGITAL:
                return new DigitalProductFactory();
            case SERVICE:
                return new ServiceProductFactory();
            default:
                throw new IllegalArgumentException("Unsupported product type: " + type);
        }
    }
}
