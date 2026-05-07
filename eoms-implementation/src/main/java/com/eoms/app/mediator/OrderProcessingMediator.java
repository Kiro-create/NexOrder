package com.eoms.app.mediator;

import com.eoms.entity.Order;
import com.eoms.factory.PaymentProcessor;

/**
 * Mediator interface for coordinating order processing workflow.
 * Encapsulates interactions between order, payment, and shipping services.
 */
public interface OrderProcessingMediator {
    /**
     * Processes the complete order workflow: finalization, payment, and shipping.
     * @param order The order to process
     * @param processor The payment processor to use
     * @return true if processing successful, false otherwise
     */
    boolean processOrder(Order order, PaymentProcessor processor);
}