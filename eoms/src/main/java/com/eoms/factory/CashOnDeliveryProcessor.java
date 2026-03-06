package com.eoms.factory;

public class CashOnDeliveryProcessor extends PaymentProcessor {

    @Override
    public PaymentMethod createPayment() {
        return new CashOnDeliveryPayment();
    }

    @Override
    public String getPaymentStatus() {
        return "Cash On Delivery";
    }
}