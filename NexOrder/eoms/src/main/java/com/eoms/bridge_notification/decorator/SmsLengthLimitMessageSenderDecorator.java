package com.eoms.bridge_notification.decorator;

import com.eoms.bridge_notification.MessageSender;

/**
 * Enforces a maximum SMS body length before delegating (avoids multi-segment charges / truncation by carrier).
 */
public class SmsLengthLimitMessageSenderDecorator extends MessageSenderDecorator {

    private final int maxLength;

    public SmsLengthLimitMessageSenderDecorator(MessageSender decoratedSender) {
        this(decoratedSender, 160);
    }

    public SmsLengthLimitMessageSenderDecorator(MessageSender decoratedSender, int maxLength) {
        super(decoratedSender);
        if (maxLength < 4) {
            throw new IllegalArgumentException("maxLength must allow at least 4 characters for truncation suffix");
        }
        this.maxLength = maxLength;
    }

    @Override
    public void sendMessage(String message) {
        String body = message;
        if (body.length() > maxLength) {
            body = body.substring(0, maxLength - 3) + "...";
        }
        super.sendMessage(body);
    }
}
