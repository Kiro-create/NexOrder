package com.eoms.bridge_notification.decorator;

import com.eoms.bridge_notification.MessageSender;

public abstract class MessageSenderDecorator implements MessageSender {

    protected MessageSender decoratedSender;

    public MessageSenderDecorator(MessageSender decoratedSender) {
        if (decoratedSender == null) {
            throw new IllegalArgumentException("Message sender must not be null");
        }
        this.decoratedSender = decoratedSender;
    }

    @Override
    public void sendMessage(String message) {
        decoratedSender.sendMessage(message);
    }
}
