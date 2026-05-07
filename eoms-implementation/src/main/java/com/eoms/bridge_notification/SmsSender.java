package com.eoms.bridge_notification;

public class SmsSender implements MessageSender{
    @Override
    public void sendMessage(String message) {
        System.out.println("Sending SMS: " + message);
    }
    
}
