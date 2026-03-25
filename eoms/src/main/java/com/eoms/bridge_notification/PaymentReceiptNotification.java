package com.eoms.bridge_notification;

public class PaymentReceiptNotification extends Notification {

    public PaymentReceiptNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.sendMessage("Payment Receipt: " + message);
    }
}