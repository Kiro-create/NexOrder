package com.eoms.inventory;

import com.eoms.entity.Product;

public class InventoryManager {

    private static InventoryManager instance;

    private InventoryManager() {}

    public static InventoryManager getInstance() {

        if (instance == null) {
            instance = new InventoryManager();
        }

        return instance;
    }

    public void addStock(Product product, int quantity) {

        product.increaseStock(quantity);
    }

    public boolean reserveStock(Product product, int quantity) {

        return product.decreaseStock(quantity);
    }

    public int checkStock(Product product) {

        return product.getStock();
    }
}