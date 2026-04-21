package com.eoms.strategy;

import com.eoms.factory.PaymentProcessor;

public class PaymentMethodAdapter implements PaymentStrategy {

    private PaymentProcessor processor;

    public PaymentMethodAdapter(PaymentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void pay(double amount) {
        processor.processOrder(amount);
    }
}