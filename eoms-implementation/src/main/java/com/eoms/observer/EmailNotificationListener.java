package com.eoms.observer;

import com.eoms.bridge_notification.MessageSender;

public class EmailNotificationListener implements OrderEventListener {

    private final MessageSender emailSender;

    public EmailNotificationListener(MessageSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void update(OrderEvent event) {
        String message = event.getMessage() + " | Order ID: " + event.getOrder().getOrderId();
        emailSender.sendMessage(message);
    }
}