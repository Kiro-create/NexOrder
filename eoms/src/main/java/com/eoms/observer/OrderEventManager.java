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
        logger.info("OrderEventManager: Subscribing listener " + listener.getClass().getSimpleName() + " to event type: " + type);
        listeners.get(type).add(listener);
        logger.info("OrderEventManager: Listener subscribed successfully. Total listeners for " + type + ": " + listeners.get(type).size());
    }

    public void unsubscribe(OrderEventType type, OrderEventListener listener) {
        logger.info("OrderEventManager: Unsubscribing listener " + listener.getClass().getSimpleName() + " from event type: " + type);
        boolean removed = listeners.get(type).remove(listener);
        if (removed) {
            logger.info("OrderEventManager: Listener unsubscribed successfully. Remaining listeners for " + type + ": " + listeners.get(type).size());
        } else {
            logger.log("OrderEventManager: Listener not found for unsubscription");
        }
    }

    public void notifyListeners(OrderEvent event) {
        logger.info("OrderEventManager: Notifying listeners for event type: " + event.getType() + ", Order ID: " + event.getOrder().getOrderId());

        List<OrderEventListener> eventListeners = listeners.get(event.getType());
        logger.info("OrderEventManager: Found " + eventListeners.size() + " listeners for event type: " + event.getType());

        for (OrderEventListener listener : eventListeners) {
            try {
                logger.info("OrderEventManager: Notifying listener: " + listener.getClass().getSimpleName());
                listener.update(event);
                logger.info("OrderEventManager: Listener " + listener.getClass().getSimpleName() + " notified successfully");
            } catch (Exception e) {
                logger.error("OrderEventManager: Error notifying listener " + listener.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }

        logger.info("OrderEventManager: Event notification completed for " + event.getType());
    }
}