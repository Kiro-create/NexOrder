package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Payment;

public class PaymentDAO implements PaymentInterface {

    private List<Payment> payments = new ArrayList<>();

    @Override
    public void savePayment(Payment payment) {

        payments.add(payment);

        System.out.println("Payment recorded. ID: " + payment.getPaymentId());
    }

    @Override
    public Payment findPaymentById(int paymentId) {
        for (Payment payment : payments) {
            if (payment.getPaymentId() == paymentId) {
                return payment;
            }
        }
        return null; // not found
    }

}