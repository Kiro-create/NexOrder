package com.eoms.bridge_notification;

public class OrderConfirmationNotification extends Notification {

    public OrderConfirmationNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.sendMessage("Order Confirmation: " + message);
    }
}