package com.eoms.DAO;

import com.eoms.entity.Payment;

public interface PaymentInterface {

    void savePayment(Payment payment);

	Payment findPaymentById(int paymentId);

	int getNextPaymentId();

	boolean deletePaymentById(int paymentId);

}