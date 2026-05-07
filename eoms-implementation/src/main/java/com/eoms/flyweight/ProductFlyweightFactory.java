package com.eoms.flyweight;

import com.eoms.config.Logger;
import com.eoms.entity.Product.ProductType;
import java.util.HashMap;
import java.util.Map;

public class ProductFlyweightFactory {
    // Intrinsic key only: ProductType is shared/stable across many Product instances.
    private static final Map<ProductType, ProductFlyweight> flyweights = new HashMap<>();

    public static ProductFlyweight getFlyweight(ProductType type) {
        Logger logger = Logger.getInstance();
        if (!flyweights.containsKey(type)) {
            logger.info("Creating new flyweight for type: " + type);
            flyweights.put(type, new ProductFlyweight(type));
        } else {
            logger.info("Reusing existing flyweight for type: " + type);
        }

        return flyweights.get(type);
    }

    public static int getFlyweightCount() {
        Logger.getInstance().info("Total unique flyweights currently: " + flyweights.size());
        return flyweights.size();
    }
}