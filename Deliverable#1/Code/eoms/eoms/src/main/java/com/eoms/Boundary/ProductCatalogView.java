package com.eoms.Boundary;

import java.util.List;
import java.util.Scanner;

import com.eoms.Controller.ManageProductCatalogController;
import com.eoms.entity.Product;

public class ProductCatalogView {

    private ManageProductCatalogController controller;
    private Scanner scanner;

    public ProductCatalogView(ManageProductCatalogController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }
    

    public void addProduct() {

        System.out.print("Product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Product name: ");
        String name = scanner.nextLine();

        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        // --- Product type selection ---
        System.out.println("Select product type:");
        System.out.println("1. PHYSICAL");
        System.out.println("2. DIGITAL");
        System.out.println("3. SERVICE");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        Product.ProductType type;
        switch (typeChoice) {
            case 1: type = Product.ProductType.PHYSICAL; break;
            case 2: type = Product.ProductType.DIGITAL; break;
            case 3: type = Product.ProductType.SERVICE; break;
            default: type = Product.ProductType.PHYSICAL; // fallback
        }

        // --- Call controller with type ---
        boolean success = controller.addProduct(id, name, price, stock, type);

        if (success) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product.");
        }
    }

    public void displayProducts() {

        List<Product> products = controller.viewProducts();

        for (Product p : products) {

            System.out.println(
                "Product: " + p.getName() +
                " | Price: " + p.getPrice() +
                " | Stock: " + p.getStock()
            );
        }
    }
    
}