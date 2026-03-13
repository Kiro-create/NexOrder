package com.eoms.service.impl;

import com.eoms.factory.PaymentProcessor;
import com.eoms.service.PaymentService;
import com.eoms.DAO.PaymentInterface;
import com.eoms.entity.Payment;
import com.eoms.entity.Order;
import com.eoms.config.Logger;

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

        double amount = order.calculateTotal();
        logger.log("Order total calculated: " + amount);

        // let the processor handle external payment logic
        processor.processOrder(amount);
        String status = processor.getPaymentStatus();
        order.setStatus(status);
        logger.info("Payment processor returned status: " + status);

        Payment payment = Payment.createPayment(paymentId, amount);
        logger.log("Payment object created with id: " + paymentId);

        payment.approvePayment();
        logger.info("Payment approved");

        paymentDAO.savePayment(payment);
        logger.log("Payment saved");

        return payment;
    }
}
