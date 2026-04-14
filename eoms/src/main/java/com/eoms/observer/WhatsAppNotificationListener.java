package com.eoms.observer;

import com.eoms.bridge_notification.MessageSender;

public class WhatsAppNotificationListener implements OrderEventListener {

    private final MessageSender whatsAppSender;

    public WhatsAppNotificationListener(MessageSender whatsAppSender) {
        this.whatsAppSender = whatsAppSender;
    }

    @Override
    public void update(OrderEvent event) {
        String message = event.getMessage() + " | Order ID: " + event.getOrder().getOrderId();
        whatsAppSender.sendMessage(message);
    }
}