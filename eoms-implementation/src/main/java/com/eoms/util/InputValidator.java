package com.eoms.util;

/**
 * Utility class for input validation in the E-Commerce OMS system.
 * Provides methods to validate common input types and throw exceptions for invalid data.
 */
public class InputValidator {

    // Maximum limits
    public static final double MAX_PRICE = 100000.0;
    public static final int MAX_STOCK = 100000;
    public static final int MAX_QUANTITY = 1000;
    public static final int MAX_STRING_LENGTH = 255;

    /**
     * Validates that an integer is positive (greater than 0).
     */
    public static void validatePositiveInt(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive. Received: " + value);
        }
    }

    /**
     * Validates that a string is not null, empty, or whitespace-only.
     */
    public static void validateNonEmptyString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }

    /**
     * Validates string length within bounds.
     */
    public static void validateStringLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " cannot exceed " + maxLength + " characters.");
        }
    }

    /**
     * Validates that an integer is within a specified range (inclusive).
     */
    public static void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max + ". Received: " + value);
        }
    }

    /**
     * Validates price with business rules.
     */
    public static void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative. Received: " + price);
        }
        if (price > MAX_PRICE) {
            throw new IllegalArgumentException("Price cannot exceed $" + MAX_PRICE);
        }
    }

    /**
     * Validates stock quantity.
     */
    public static void validateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative. Received: " + stock);
        }
        if (stock > MAX_STOCK) {
            throw new IllegalArgumentException("Stock cannot exceed " + MAX_STOCK + " units.");
        }
    }

    /**
     * Validates order quantity.
     */
    public static void validateQuantity(int quantity) {
        validatePositiveInt(quantity, "Quantity");
        if (quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Quantity cannot exceed " + MAX_QUANTITY + " units.");
        }
    }

    /**
     * Validates that an object is not null.
     */
    public static void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
    }

}