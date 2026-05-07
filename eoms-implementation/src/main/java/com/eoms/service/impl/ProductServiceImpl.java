package com.eoms.service.impl;

import com.eoms.service.ProductService;
import com.eoms.DAO.ProductInterface;
import com.eoms.entity.Product;
import com.eoms.config.Logger;
import java.util.List;
import com.eoms.util.InputValidator;

/**
 * Concrete service implementation. Keeps all business rules related to products.
 * This class conforms to SRP by focusing strictly on product operations.
 */
public class ProductServiceImpl implements ProductService {
    private final ProductInterface productDAO;

    public ProductServiceImpl(ProductInterface productDAO) {
        if (productDAO == null) {
            throw new IllegalArgumentException("productDAO must not be null");
        }
        this.productDAO = productDAO;
    }

    @Override
    public boolean addProduct(int id, String name, double price, int stock, Product.ProductType type) {
        InputValidator.validatePositiveInt(id, "Product ID");
        InputValidator.validateNonEmptyString(name, "Product name");
        InputValidator.validateStringLength(name, InputValidator.MAX_STRING_LENGTH, "Product name");
        InputValidator.validatePrice(price);
        InputValidator.validateStock(stock);
        InputValidator.validateNotNull(type, "Product type");

        Logger logger = Logger.getInstance();
        logger.info("Adding new product with ID: " + id);

        // ✅ uniqueness check HERE
        Product existingProduct = productDAO.findProductById(id);
        if (existingProduct != null) {
            logger.error("Product already created with ID: " + id);
            throw new IllegalArgumentException("Product already created with ID: " + id);
        }

        Product product = Product.createProduct(id, name, price, stock, type);
        productDAO.saveProduct(product);

        logger.log("Product saved successfully: " + name);
        return true;
    }

    @Override
    public List<Product> getAllProducts() {
        Logger logger = Logger.getInstance();
        logger.info("Viewing product catalog");
        return productDAO.getAllProducts();
    }
}
