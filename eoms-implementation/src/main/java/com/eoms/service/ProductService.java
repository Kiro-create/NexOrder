package com.eoms.service;

import com.eoms.entity.Product;
import com.eoms.entity.Product.ProductType;
import java.util.List;

/**
 * Business layer interface for product catalog operations.  
 * Defined to decouple presentation from the DAO layer (DIP).
 */
public interface ProductService {
    boolean addProduct(int id, String name, double price, int stock, ProductType type);
    List<Product> getAllProducts();
}
