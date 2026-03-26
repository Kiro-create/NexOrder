package com.eoms.app;

import com.eoms.factory.PaymentProcessor;

/**
 * Abstraction for providing payment processors (DIP):
 * UI handlers ask for a processor without knowing concrete wiring details.
 */
public interface PaymentProcessorProvider {
    PaymentProcessor getProcessor(int paymentChoice);
}

