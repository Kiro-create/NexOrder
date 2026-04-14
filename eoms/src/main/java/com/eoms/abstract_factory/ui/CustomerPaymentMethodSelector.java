package com.eoms.abstract_factory.ui;

import java.util.Scanner;

/**
 * Customer-specific payment method selector.
 * Provides console-based payment method selection for customers.
 */
public class CustomerPaymentMethodSelector implements PaymentMethodSelector {

    @Override
    public int selectPaymentMethod(Scanner scanner) {
        System.out.println("Choose payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. PayPal");
        System.out.println("3. Cash On Delivery");
        System.out.println("0. Cancel");
        
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return choice;
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // clear invalid input
            return -1; // invalid choice
        }
    }
}