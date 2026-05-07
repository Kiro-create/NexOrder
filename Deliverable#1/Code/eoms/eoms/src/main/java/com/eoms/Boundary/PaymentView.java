package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.Controller.ProcessPaymentController;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;

public class PaymentView {

    private ProcessPaymentController controller;
    private Scanner scanner;

    public PaymentView(ProcessPaymentController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public Payment makePayment(Order order) {

        System.out.print("Enter Payment ID: ");
        int paymentId = scanner.nextInt();
        scanner.nextLine();

        Payment payment = controller.processPayment(paymentId, order);

        System.out.println("Payment approved. Amount = " + payment.getAmount());

        return payment;
    }
}