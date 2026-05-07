package com.eoms.factory;

public class CreditCardProcessor extends PaymentProcessor {

    @Override
    public PaymentMethod createPayment() {
        return new CreditCardPayment();
    }

    @Override
    public String getPaymentStatus() {
        return "Paid with Credit Card";
    }
}