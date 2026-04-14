package com.eoms.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderEventManager {
    private static OrderEventManager instance;

    private final Map<OrderEventType, List<OrderEventListener>> listeners;

    private OrderEventManager() {
        listeners = new HashMap<>();
        for (OrderEventType type : OrderEventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
    }

    public static synchronized OrderEventManager getInstance() {
        if (instance == null) {
            instance = new OrderEventManager();
        }
        return instance;
    }

    public void subscribe(OrderEventType type, OrderEventListener listener) {
        listeners.get(type).add(listener);
    }

    public void unsubscribe(OrderEventType type, OrderEventListener listener) {
        listeners.get(type).remove(listener);
    }

    public void notifyListeners(OrderEvent event) {
        System.out.println("[EVENT] " + event.getType() + " triggered");

        List<OrderEventListener> eventListeners = listeners.get(event.getType());
        for (OrderEventListener listener : eventListeners) {
            listener.update(event);
        }
    }
}