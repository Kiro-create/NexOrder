package com.eoms.Boundary;

import java.util.List;
import java.util.Scanner;

<<<<<<< HEAD
import com.eoms.service.ProductService;
import com.eoms.entity.Product;

/**
 * Presentation layer: interacts with user and delegates business work to a service.
 */
public class ProductCatalogView {

    private ProductService productService;
    private Scanner scanner;

    public ProductCatalogView(ProductService productService, Scanner scanner) {
        this.productService = productService;
        this.scanner = scanner;
    }
    
=======
import com.eoms.Controller.ManageProductCatalogController;
import com.eoms.entity.Product;

public class ProductCatalogView {

    private ManageProductCatalogController controller;
    private Scanner scanner;

    public ProductCatalogView(ManageProductCatalogController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

    public void addProduct() {

        System.out.print("Product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Product name: ");
        String name = scanner.nextLine();

        System.out.print("Price: ");
        double price = scanner.nextDouble();
<<<<<<< HEAD
        scanner.nextLine();
=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

<<<<<<< HEAD
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
        boolean success = productService.addProduct(id, name, price, stock, type);
=======
        boolean success = controller.addProduct(id, name, price, stock);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        if (success) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product.");
        }
    }

    public void displayProducts() {

<<<<<<< HEAD
        List<Product> products = productService.getAllProducts();
=======
        List<Product> products = controller.viewProducts();
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        for (Product p : products) {

            System.out.println(
                "Product: " + p.getName() +
                " | Price: " + p.getPrice() +
                " | Stock: " + p.getStock()
            );
        }
    }
<<<<<<< HEAD
    
=======
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
}