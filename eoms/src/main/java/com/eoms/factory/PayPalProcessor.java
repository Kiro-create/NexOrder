package com.eoms.factory;

public class PayPalProcessor extends PaymentProcessor {

    @Override
    public PaymentMethod createPayment() {
        return new PayPalPayment();
    }

    @Override
    public String getPaymentStatus() {
        return "Paid with PayPal";
    }
}