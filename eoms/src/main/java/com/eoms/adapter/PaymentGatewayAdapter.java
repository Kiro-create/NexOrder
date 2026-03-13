package com.eoms.adapter;

import com.eoms.factory.PaymentProcessor;
import com.eoms.factory.PaymentMethod;
import com.eoms.externalservice.StripeGateway;

public class PaymentGatewayAdapter extends PaymentProcessor {

    private PaymentProcessor processor;
    private StripeGateway stripe;

    public PaymentGatewayAdapter(PaymentProcessor processor) {
        this.processor = processor;
        this.stripe = new StripeGateway();
    }

    @Override
    public PaymentMethod createPayment() {

        PaymentMethod original = processor.createPayment();
    

        return new PaymentMethod() {

            @Override
            public void processPayment(double amount) {

                // run the original payment logic
                original.processPayment(amount);
                

                // then call the external API
                stripe.makeTransaction(amount);

            }
        };
    }

    @Override
    public String getPaymentStatus() {
        return processor.getPaymentStatus();
    }
}