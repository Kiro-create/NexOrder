package com.eoms.factory;

public abstract class PaymentProcessor {

    public void processOrder(double amount) {

        PaymentMethod payment = createPayment();

        payment.processPayment(amount);

    }

    public abstract PaymentMethod createPayment();

    public abstract String getPaymentStatus();
}