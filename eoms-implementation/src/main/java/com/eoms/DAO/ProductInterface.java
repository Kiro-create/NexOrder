package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Product;

public interface ProductInterface {

    void saveProduct(Product product);

    List<Product> getAllProducts();

    Product findProductById(int productId);

}