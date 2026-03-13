package com.eoms.factory;

public class CashOnDeliveryPayment implements PaymentMethod {

    @Override
    public void processPayment(double amount) {
        System.out.println("Cash on Delivery selected. Amount to pay: " + amount);
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
