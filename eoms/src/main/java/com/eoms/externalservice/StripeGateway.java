package com.eoms.externalservice;

public class StripeGateway {

    public void makeTransaction(double amount){
        System.out.println("Stripe processed payment: " + amount);
    }

}