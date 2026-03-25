package com.eoms.config;

import com.eoms.abstract_factory.ui.AdminUIFactory;
import com.eoms.abstract_factory.ui.CustomerUIFactory;
import com.eoms.abstract_factory.ui.UIFactory;
import com.eoms.abstract_factory.ui.UserRole;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Registry for UI factories used by the console application.  
 * Extracted from Main to reduce its responsibilities (SRP) and allow
 * client code to register additional roles at runtime (OCP).
 */
public final class UIFactoryRegistry {
    private static final Map<String, Supplier<UIFactory>> UI_FACTORY_REGISTRY = new LinkedHashMap<>();

    static {
        registerUIFactory(UserRole.ADMIN.name(), AdminUIFactory::new);
        registerUIFactory(UserRole.CUSTOMER.name(), CustomerUIFactory::new);
    }

    private UIFactoryRegistry() {
        // prevent instantiation
    }

    public static synchronized void registerUIFactory(String role, Supplier<UIFactory> factorySupplier) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be null or empty.");
        }
        if (factorySupplier == null) {
            throw new IllegalArgumentException("Factory supplier must not be null.");
        }

        UI_FACTORY_REGISTRY.put(normalizeRole(role), factorySupplier);
    }

    public static UIFactory getUIFactory(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null.");
        }
        return getUIFactory(role.name());
    }

    public static synchronized UIFactory getUIFactory(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be null or empty.");
        }

        String normalizedRole = normalizeRole(role);
        Supplier<UIFactory> factorySupplier = UI_FACTORY_REGISTRY.get(normalizedRole);

        if (factorySupplier == null) {
            throw new IllegalArgumentException(
                    "Unsupported role: " + role + ". Supported roles: " + supportedUIRoles());
        }

        return factorySupplier.get();
    }

    public static synchronized Set<String> supportedUIRoles() {
        return Set.copyOf(UI_FACTORY_REGISTRY.keySet());
    }

    private static String normalizeRole(String role) {
        return role.trim().toUpperCase(Locale.ROOT);
    }
}
