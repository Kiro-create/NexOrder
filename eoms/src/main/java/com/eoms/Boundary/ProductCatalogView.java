package com.eoms.Boundary;

import java.util.List;
import java.util.Scanner;

import com.eoms.service.ProductService;
import com.eoms.entity.Product;
import com.eoms.util.InputValidator;

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
    

    public void addProduct() {
        try {
            System.out.print("Product ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            InputValidator.validatePositiveInt(id, "Product ID");

            System.out.print("Product name: ");
            String name = scanner.nextLine().trim();
            InputValidator.validateNonEmptyString(name, "Product name");
            InputValidator.validateStringLength(name, InputValidator.MAX_STRING_LENGTH, "Product name");

            System.out.print("Price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            InputValidator.validatePrice(price);

            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());
            InputValidator.validateStock(stock);

            // --- Product type selection ---
            System.out.println("Select product type:");
            System.out.println("1. PHYSICAL");
            System.out.println("2. DIGITAL");
            System.out.println("3. SERVICE");
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            InputValidator.validateRange(typeChoice, 1, 3, "Product type choice");

            Product.ProductType type;
            switch (typeChoice) {
                case 1: type = Product.ProductType.PHYSICAL; break;
                case 2: type = Product.ProductType.DIGITAL; break;
                case 3: type = Product.ProductType.SERVICE; break;
                default: 
                    // This should not happen due to validation above
                    System.out.println("Invalid type choice, defaulting to PHYSICAL.");
                    type = Product.ProductType.PHYSICAL;
            }

            // --- Call controller with type ---
            boolean success = productService.addProduct(id, name, price, stock, type);

            if (success) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void displayProducts() {

        List<Product> products = productService.getAllProducts();

        for (Product p : products) {

            System.out.println(
                "Product: " + p.getName() +
                " | Price: " + p.getPrice() +
                " | Stock: " + p.getStock()
            );
        }
    }
    
}