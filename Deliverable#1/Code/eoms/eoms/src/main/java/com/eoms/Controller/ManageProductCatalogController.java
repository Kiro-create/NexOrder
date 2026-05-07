package com.eoms.Controller;

import java.util.*;
import com.eoms.DAO.ProductInterface;
import com.eoms.entity.Product;
import com.eoms.config.Logger;
import com.eoms.inventory.InventoryManager;

public class ManageProductCatalogController {

    private ProductInterface productDAO;

    public ManageProductCatalogController(ProductInterface productDAO) {
        this.productDAO = productDAO;
    }

    public boolean addProduct(int id, String name, double price, int stock, Product.ProductType type) {
        Logger logger = Logger.getInstance();
        logger.info("Adding new product with ID: " + id);

        // create product with type
        Product product = Product.createProduct(id, name, price, stock, type);

        productDAO.saveProduct(product);

        logger.log("Product saved successfully: " + name);

        return true;
    }

    public List<Product> viewProducts() {

        Logger logger = Logger.getInstance();

        logger.info("Viewing product catalog");

        return productDAO.getAllProducts();
    }
}