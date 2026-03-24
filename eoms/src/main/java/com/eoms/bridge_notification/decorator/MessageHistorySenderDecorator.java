package com.eoms.bridge_notification.decorator;

import com.eoms.bridge_notification.MessageSender;

import java.util.ArrayList;
import java.util.List;

public class MessageHistorySenderDecorator extends MessageSenderDecorator {

    private final List<String> messageHistory = new ArrayList<>();

    public MessageHistorySenderDecorator(MessageSender decoratedSender) {
        super(decoratedSender);
    }

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
        messageHistory.add(message);
    }

    public List<String> getMessageHistory() {
        return new ArrayList<>(messageHistory);
    }
}
