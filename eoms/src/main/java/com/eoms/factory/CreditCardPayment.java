package com.eoms.factory;

public class CreditCardPayment implements PaymentMethod {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Credit Card payment: " + amount);
    }

}