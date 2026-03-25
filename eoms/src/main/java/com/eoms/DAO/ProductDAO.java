package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Product;

public class ProductDAO implements ProductInterface {

    private List<Product> products = new ArrayList<>();

    @Override
    public void saveProduct(Product product) {

        products.add(product);

        System.out.println("Product stored: " + product.getName());
    }

    @Override
    public List<Product> getAllProducts() {

        return products;
    }

    @Override
    public Product findProductById(int productId) {

        for (Product p : products) {

            if (p.getProductId() == productId) {
                return p;
            }

        }

        return null;
    }

}