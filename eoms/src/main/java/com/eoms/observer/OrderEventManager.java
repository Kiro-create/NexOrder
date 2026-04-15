package com.eoms.observer;

import com.eoms.config.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderEventManager {
    private static OrderEventManager instance;
    private final Logger logger;

    private final Map<OrderEventType, List<OrderEventListener>> listeners;

    private OrderEventManager() {
        listeners = new HashMap<>();
        for (OrderEventType type : OrderEventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
        logger = Logger.getInstance();
        logger.info("OrderEventManager: Singleton instance created and initialized");
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
        List<OrderEventListener> eventListeners = listeners.get(event.getType());

        for (OrderEventListener listener : eventListeners) {
            try {
                listener.update(event);
            } catch (Exception e) {
                logger.error("OrderEventManager: Error notifying listener " + listener.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}