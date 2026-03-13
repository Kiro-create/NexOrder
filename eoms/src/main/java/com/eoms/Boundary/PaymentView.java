package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.service.PaymentService;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;

public class PaymentView {

    private PaymentService paymentService;
    private Scanner scanner;

    public PaymentView(PaymentService paymentService, Scanner scanner) {
        this.paymentService = paymentService;
        this.scanner = scanner;
    }

    public Payment makePayment(Order order, com.eoms.factory.PaymentProcessor processor) {

        System.out.print("Enter Payment ID: ");
        int paymentId = scanner.nextInt();
        scanner.nextLine();

        Payment payment = paymentService.processPayment(paymentId, order, processor);

        System.out.println("Payment approved. Amount = " + payment.getAmount());

        return payment;
    }
}