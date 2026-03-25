package com.eoms.bridge_notification.decorator;

import com.eoms.bridge_notification.MessageSender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adds a send timestamp to the message body for audit/support (email notification pipeline).
 */
public class TimestampMessageSenderDecorator extends MessageSenderDecorator {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TimestampMessageSenderDecorator(MessageSender decoratedSender) {
        super(decoratedSender);
    }

    @Override
    public void sendMessage(String message) {
        String stamped = "[" + LocalDateTime.now().format(FORMAT) + "] " + message;
        super.sendMessage(stamped);
    }
}
