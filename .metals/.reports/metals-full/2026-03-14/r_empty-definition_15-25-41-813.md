error id: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/DAO/ProductDAO.java:com/eoms/entity/Product#
file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/DAO/ProductDAO.java
empty definition using pc, found symbol in pc: com/eoms/entity/Product#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 69
uri: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/E-Commerce-OMS/NexOrder/eoms/src/main/java/com/eoms/DAO/ProductDAO.java
text:
```scala
package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.@@Product;

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
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/eoms/entity/Product#