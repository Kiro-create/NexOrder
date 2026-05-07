package com.eoms.app;

import com.eoms.factory.PaymentProcessor;
import com.eoms.strategy.PaymentContext;

/**
 * Abstraction for providing payment processors (DIP):
 * UI handlers ask for a processor without knowing concrete wiring details.
 */
public interface PaymentProcessorProvider {
    PaymentProcessor getProcessor(int paymentChoice);
     PaymentContext getPaymentContext(int paymentChoice);
}

