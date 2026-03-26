package com.eoms.service.impl;

import com.eoms.service.PaymentService;
import com.eoms.DAO.PaymentInterface;
import com.eoms.entity.Payment;
import com.eoms.entity.Order;
import com.eoms.config.Logger;
import com.eoms.factory.PaymentProcessor;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentInterface paymentDAO;

    public PaymentServiceImpl(PaymentInterface paymentDAO) {
        if (paymentDAO == null) {
            throw new IllegalArgumentException("paymentDAO must not be null");
        }
        this.paymentDAO = paymentDAO;
    }

    @Override
    public Payment processPayment(int paymentId, Order order, PaymentProcessor processor) {
        Logger logger = Logger.getInstance();
        logger.info("Starting payment process");

        // Keep a single source of truth for payable amount.
        double amount = order.getTotal() > 0 ? order.getTotal() : order.calculateTotal();
        logger.log("Order total calculated: " + amount);

        Payment payment = Payment.createPayment(paymentId, amount);
        logger.log("Payment object created with id: " + paymentId);

        try {
            // let the processor handle external payment logic
            processor.processOrder(amount);
        } catch (RuntimeException ex) {
            String declinedStatus = "Declined";
            if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
                declinedStatus = ex.getMessage();
            }
            order.setStatus(declinedStatus);
            logger.error("Payment declined. Reason: " + declinedStatus);
            return payment;
        }

        String status = processor.getPaymentStatus();
        order.setStatus(status);
        logger.info("Payment processor returned status: " + status);

        payment.approvePayment();
        logger.info("Payment approved");

        paymentDAO.savePayment(payment);
        logger.log("Payment saved");

        return payment;
    }
}
