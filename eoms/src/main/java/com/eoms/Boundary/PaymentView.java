package com.eoms.Boundary;

import java.util.Scanner;

<<<<<<< HEAD
import com.eoms.service.PaymentService;
=======
import com.eoms.Controller.ProcessPaymentController;
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
import com.eoms.entity.Order;
import com.eoms.entity.Payment;

public class PaymentView {

<<<<<<< HEAD
    private PaymentService paymentService;
    private Scanner scanner;

    public PaymentView(PaymentService paymentService, Scanner scanner) {
        this.paymentService = paymentService;
        this.scanner = scanner;
    }

    public Payment makePayment(Order order, com.eoms.factory.PaymentProcessor processor) {
=======
    private ProcessPaymentController controller;
    private Scanner scanner;

    public PaymentView(ProcessPaymentController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public Payment makePayment(Order order) {
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        System.out.print("Enter Payment ID: ");
        int paymentId = scanner.nextInt();
        scanner.nextLine();

<<<<<<< HEAD
        Payment payment = paymentService.processPayment(paymentId, order, processor);
=======
        Payment payment = controller.processPayment(paymentId, order);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        System.out.println("Payment approved. Amount = " + payment.getAmount());

        return payment;
    }
}