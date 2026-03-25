package com.eoms.bridge_notification;

public abstract class Notification {

    protected MessageSender sender;

    public Notification(MessageSender sender) {
        this.sender = sender;
    }

    public abstract void send(String message);
}