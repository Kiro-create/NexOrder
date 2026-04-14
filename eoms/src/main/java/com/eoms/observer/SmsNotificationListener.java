package com.eoms.observer;

import com.eoms.bridge_notification.MessageSender;

public class SmsNotificationListener implements OrderEventListener {

    private final MessageSender smsSender;

    public SmsNotificationListener(MessageSender smsSender) {
        this.smsSender = smsSender;
    }

    @Override
    public void update(OrderEvent event) {
        String message = event.getMessage() + " | Order ID: " + event.getOrder().getOrderId();
        smsSender.sendMessage(message);
    }
}