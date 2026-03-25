error id: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/service/impl/ProductServiceImpl.java:com/eoms/config/Logger#
file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/service/impl/ProductServiceImpl.java
empty definition using pc, found symbol in pc: com/eoms/config/Logger#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 170
uri: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/service/impl/ProductServiceImpl.java
text:
```scala
package com.eoms.service.impl;

import com.eoms.service.ProductService;
import com.eoms.DAO.ProductInterface;
import com.eoms.entity.Product;
import com.eoms.config.@@Logger;
import java.util.List;

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
        Logger logger = Logger.getInstance();
        logger.info("Adding new product with ID: " + id);

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

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/eoms/config/Logger#