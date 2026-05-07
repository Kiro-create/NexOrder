package com.eoms.config;

import com.eoms.inventory.InventoryManager;
import com.eoms.observer.OrderEventManager;

/**
 * Centralized initializer for all singletons in the application.
 * Ensures they are initialized only once, in the correct order, to avoid
 * race conditions.
 */
public class SingletonInitializer {
    private static boolean initialized = false;

    /**
     * Initializes all singletons if not already done.
     * Call this early in application startup (e.g., in EomsApplication.run()).
     */
    public static void initialize() {
        if (initialized) {
            return;  // Prevent re-initialization
        }

        // Initialize singletons in dependency order
        Logger.getInstance();  // Logger first, as others might log
        InventoryManager.getInstance();
        OrderEventManager.getInstance();

        initialized = true;
    }
}