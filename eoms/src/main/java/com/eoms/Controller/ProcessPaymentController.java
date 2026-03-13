package com.eoms.Controller;

import com.eoms.DAO.PaymentInterface;
import com.eoms.entity.Payment;
import com.eoms.entity.Order;
import com.eoms.config.Logger;

public class ProcessPaymentController {

    private PaymentInterface paymentDAO;

    public ProcessPaymentController(PaymentInterface paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public Payment processPayment(int paymentId, Order order) {

        Logger logger = Logger.getInstance();

        logger.info("Starting payment process");

        double amount = order.calculateTotal();
        logger.log("Order total calculated: " + amount);

        Payment payment = Payment.createPayment(paymentId, amount);
        logger.log("Payment object created with id: " + paymentId);

        payment.approvePayment();
        logger.info("Payment approved");

        paymentDAO.savePayment(payment);
        logger.log("Payment saved");

        return payment;
    }
}