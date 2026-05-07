package com.eoms.factory.decorator;

import com.eoms.config.Logger;
import com.eoms.factory.PaymentMethod;

/**
 * Base Decorator: implements the component interface and delegates to the wrapped object.
 */
public abstract class PaymentProcessorDecorator implements PaymentMethod {

    protected final PaymentMethod wrapped;

    public PaymentProcessorDecorator(PaymentMethod wrapped) {
        if (wrapped == null) {
            Logger.getInstance().error("PaymentProcessorDecorator: wrapped PaymentMethod is null");
            throw new IllegalArgumentException("wrapped must not be null");
        }
        this.wrapped = wrapped;
    }

    @Override
    public void processPayment(double amount) {
        wrapped.processPayment(amount);
    }
}
