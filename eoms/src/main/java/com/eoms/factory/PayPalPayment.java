package com.eoms.factory;

public class PayPalPayment implements PaymentMethod {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: " + amount);
    }

}
