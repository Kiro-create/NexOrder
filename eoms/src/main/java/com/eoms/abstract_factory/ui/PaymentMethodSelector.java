package com.eoms.abstract_factory.ui;

import java.util.Scanner;

/**
 * Interface for payment method selection UI components.
 * Abstracts the presentation of payment method choices from business logic.
 */
public interface PaymentMethodSelector {
    
    /**
     * Displays payment method options and gets user selection.
     * 
     * @param scanner Scanner for user input
     * @return selected payment method choice (0 for cancel, 1-3 for methods)
     */
    int selectPaymentMethod(Scanner scanner);
}