package com.eoms.factory;

public class CashOnDeliveryPayment implements PaymentMethod {

    @Override
    public void processPayment(double amount) {
        System.out.println("Cash on Delivery selected. Amount to pay: " + amount);
    }

}