package com.eoms.service;

import com.eoms.entity.Payment;
import com.eoms.entity.Order;
import com.eoms.factory.PaymentProcessor;

public interface PaymentService {
    /**
     * Execute a payment for the given order using the supplied processor.
     * Service is responsible for creating and persisting the Payment object and
     * for updating the order's status according to the processor result.
     */
    Payment processPayment(int paymentId, Order order, PaymentProcessor processor);
    Payment getPaymentById(int paymentId);
    boolean updatePaymentStatus(int paymentId, String status);
    boolean deletePaymentById(int paymentId);
}
