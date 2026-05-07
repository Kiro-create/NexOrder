package com.eoms.entity;

public class Payment {

    private int paymentId;
    private double amount;
    private String status;

    private Payment(int paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = "Pending";
    }

    public static Payment createPayment(int paymentId, double amount) {
        return new Payment(paymentId, amount);
    }

    public void approvePayment() {
        status = "Approved";
    }

    public int getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}