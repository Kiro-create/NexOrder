package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.service.PaymentService;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;
import com.eoms.factory.PaymentProcessor;
import com.eoms.util.InputValidator;

public class PaymentView {

    private PaymentService paymentService;
    private Scanner scanner;

    public PaymentView(PaymentService paymentService, Scanner scanner) {
        this.paymentService = paymentService;
        this.scanner = scanner;
    }

    public Payment makePayment(Order order, PaymentProcessor processor) {
        try {
            InputValidator.validateNotNull(order, "Order");
            InputValidator.validateNotNull(processor, "Payment Processor");

            System.out.print("Enter Payment ID: ");
            int paymentId = scanner.nextInt();
            InputValidator.validatePositiveInt(paymentId, "Payment ID");
            scanner.nextLine();

            Payment payment = paymentService.processPayment(paymentId, order, processor);

            if ("Approved".equals(payment.getStatus())) {
                System.out.println("Payment approved. Amount = " + payment.getAmount());
            } else {
                System.out.println("Payment not approved. Amount = " + payment.getAmount());
            }

            return payment;
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }
}