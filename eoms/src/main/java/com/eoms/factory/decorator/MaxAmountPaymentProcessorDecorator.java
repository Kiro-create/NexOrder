package com.eoms.factory.decorator;

import com.eoms.config.Logger;
import com.eoms.factory.PaymentMethod;

/**
 * Concrete Decorator: blocks payments that exceed a maximum amount (e.g., COD cap).
 */
public class MaxAmountPaymentProcessorDecorator extends PaymentProcessorDecorator {

    private final double maxAmount;

    public MaxAmountPaymentProcessorDecorator(PaymentMethod wrapped, double maxAmount) {
        super(wrapped);
        if (maxAmount < 0) {
            Logger.getInstance().error("MaxAmountPaymentProcessorDecorator: maxAmount must be >= 0");
            throw new IllegalArgumentException("maxAmount must be >= 0");
        }
        this.maxAmount = maxAmount;
    }

    @Override
    public void processPayment(double amount) {
        if (amount > maxAmount) {
            Logger.getInstance().error("Payment declined: amount " + amount + " exceeds max allowed " + maxAmount);
            throw new IllegalStateException("Declined (exceeds COD cap of " + maxAmount + ")");
        }
        super.processPayment(amount);
    }
}
