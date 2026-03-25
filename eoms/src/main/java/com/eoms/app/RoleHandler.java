package com.eoms.app;

import java.util.Scanner;

/**
 * Encapsulates logic for a particular role (admin, customer, etc.).
 * Implementations are responsible for interacting with the user via provided
 * views/services and running until the role session ends.  This keeps the
 * application entry point (Main) free of business details (SRP).
 */
public interface RoleHandler {
    /**
     * Execute the role-specific workflow.  The scanner is supplied by the
     * caller to avoid each handler creating their own instance.
     */
    void handle(Scanner scanner);
}
