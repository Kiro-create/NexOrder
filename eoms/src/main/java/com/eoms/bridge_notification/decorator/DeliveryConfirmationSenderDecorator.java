package com.eoms.bridge_notification.decorator;

import com.eoms.bridge_notification.MessageSender;

public class DeliveryConfirmationSenderDecorator extends MessageSenderDecorator {

    public DeliveryConfirmationSenderDecorator(MessageSender decoratedSender) {
        super(decoratedSender);
    }

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
        System.out.println("Delivery confirmation generated for: " + message);
    }
}
