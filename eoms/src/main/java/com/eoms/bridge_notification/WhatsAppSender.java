package com.eoms.bridge_notification;

public class WhatsAppSender implements MessageSender {
    @Override
    public void sendMessage(String message) {
        System.out.println("Sending WhatsApp: " + message);
    }
}
