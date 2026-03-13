package com.eoms.bridge_notification;

public class ShippingUpdateNotification extends Notification {

    public ShippingUpdateNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.sendMessage("Shipping Update: " + message);
    }
}