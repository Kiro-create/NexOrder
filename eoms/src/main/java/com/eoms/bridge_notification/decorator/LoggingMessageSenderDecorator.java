package com.eoms.bridge_notification.decorator;

import com.eoms.bridge_notification.MessageSender;
import com.eoms.config.Logger;

public class LoggingMessageSenderDecorator extends MessageSenderDecorator {

    public LoggingMessageSenderDecorator(MessageSender decoratedSender) {
        super(decoratedSender);
    }

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
        Logger.getInstance().info("Notification message sent: " + message);
    }
}
