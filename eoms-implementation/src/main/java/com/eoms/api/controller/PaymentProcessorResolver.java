package com.eoms.api.controller;

import com.eoms.adapter.PaymentGatewayAdapter;
import com.eoms.factory.*;

public class PaymentProcessorResolver {

    public static PaymentProcessor resolve(String method) {
        if (method == null) {
            return null;
        }

        switch (method.toUpperCase()) {
            case "CREDIT_CARD":
                return new PaymentGatewayAdapter(new CreditCardProcessor());

            case "PAYPAL":
                return new PaymentGatewayAdapter(new PayPalProcessor());

            case "CASH_ON_DELIVERY":
                return new CashOnDeliveryProcessor();

            default:
                return null;
        }
    }
}