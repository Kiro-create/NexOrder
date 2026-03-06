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

        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        boolean success = controller.addProduct(id, name, price, stock);

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