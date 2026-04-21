package com.eoms.template_method;

import com.eoms.config.Logger;
import com.eoms.entity.Order;
import com.eoms.factory.PaymentProcessor;
import com.eoms.util.InputValidator;

/**
 * Template Method pattern for order processing workflow.
 * Defines the skeleton of the order processing algorithm.
 */
public abstract class OrderProcessingTemplate {

    protected final Logger logger = Logger.getInstance();

    /**
     * Template method: Defines the invariant order processing workflow.
     *
     * Steps (with required logging):
     * - validation
     * - payment
     * - processing (calculate) + finalize (only after accepted payment)
     * - shipment
     * - notifications/events
     */
    public final boolean processOrder(Order order, PaymentProcessor processor) {
        InputValidator.validateNotNull(order, "Order");
        InputValidator.validateNotNull(processor, "PaymentProcessor");

        logger.info("OrderProcessingTemplate: Starting workflow for order ID: " + order.getOrderId());

        try {
            logger.info("OrderProcessingTemplate: Step=validation (order ID: " + order.getOrderId() + ")");
            if (!validateOrder(order)) {
                logger.error("OrderProcessingTemplate: Validation failed (order ID: " + order.getOrderId() + ")");
                return false;
            }

            logger.info("OrderProcessingTemplate: Step=processing (calculate) (order ID: " + order.getOrderId() + ")");
            double total = calculateTotal(order);
            logger.info("OrderProcessingTemplate: Processing completed. total=" + total + " (order ID: " + order.getOrderId() + ")");

            logger.info("OrderProcessingTemplate: Step=payment (order ID: " + order.getOrderId() + ")");
            if (!handlePayment(order, processor)) {
                logger.error("OrderProcessingTemplate: Payment failed (order ID: " + order.getOrderId() + ")");
                return false;
            }

            logger.info("OrderProcessingTemplate: Step=finalize (after payment accepted) (order ID: " + order.getOrderId() + ")");
            if (!finalizeOrder(order)) {
                logger.error("OrderProcessingTemplate: Finalize failed (order ID: " + order.getOrderId() + ")");
                return false;
            }

            logger.info("OrderProcessingTemplate: Step=shipment (order ID: " + order.getOrderId() + ")");
            if (!createShipment(order)) {
                logger.error("OrderProcessingTemplate: Shipment failed (order ID: " + order.getOrderId() + ")");
                return false;
            }

            logger.info("OrderProcessingTemplate: Step=notifications/events (order ID: " + order.getOrderId() + ")");
            sendNotifications(order);

            logger.info("OrderProcessingTemplate: Workflow completed successfully (order ID: " + order.getOrderId() + ")");
            return true;
        } catch (Exception e) {
            logger.error("OrderProcessingTemplate: Unexpected error (order ID: " + order.getOrderId() + "): " + e.getMessage());
            return false;
        }
    }

    protected abstract boolean validateOrder(Order order);

    /**
     * Processing step for the template method.
     * Calculates total with type-specific policies.
     */
    protected abstract double calculateTotal(Order order);

    protected abstract boolean handlePayment(Order order, PaymentProcessor processor);

    /**
     * Finalize the order only after payment is accepted.
     */
    protected abstract boolean finalizeOrder(Order order);

    protected abstract boolean createShipment(Order order);

    /**
     * Hook step for observer/event notifications.
     * If underlying services already publish events, this method should not duplicate them.
     */
    protected abstract void sendNotifications(Order order);
}

