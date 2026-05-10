package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Payment;

public class PaymentDAO implements PaymentInterface {

    private List<Payment> payments = new ArrayList<>();
    private int nextPaymentId = 1;

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

    @Override
    public int getNextPaymentId() {
        return nextPaymentId++;
    }

    @Override
    public boolean deletePaymentById(int paymentId) {
        return payments.removeIf(payment -> payment.getPaymentId() == paymentId);
    }

}