package com.eoms.factory;

public class PayPalPayment implements PaymentMethod {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: " + amount);
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
