package com.eoms.template_method;

import com.eoms.DAO.PaymentInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.abstract_factory.DigitalProductFactory;
import com.eoms.abstract_factory.InvoicePolicy;
import com.eoms.entity.Order;
import com.eoms.entity.OrderItem;
import com.eoms.entity.Payment;
import com.eoms.factory.PaymentProcessor;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.util.InputValidator;

/**
 * Concrete Template Method implementation for digital product orders.
 * Shipment is a no-op, payment and events still run through existing services.
 */
public class DigitalOrderProcessing extends OrderProcessingTemplate {

    private final DigitalProductFactory factory = new DigitalProductFactory();
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentInterface paymentDAO;
    @SuppressWarnings("unused")
    private final ShipmentInterface shipmentDAO;

    public DigitalOrderProcessing(
            OrderService orderService,
            PaymentService paymentService,
            PaymentInterface paymentDAO,
            ShipmentInterface shipmentDAO
    ) {
        InputValidator.validateNotNull(orderService, "OrderService");
        InputValidator.validateNotNull(paymentService, "PaymentService");
        InputValidator.validateNotNull(paymentDAO, "PaymentDAO");
        InputValidator.validateNotNull(shipmentDAO, "ShipmentDAO");
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.paymentDAO = paymentDAO;
        this.shipmentDAO = shipmentDAO;
    }

    @Override
    protected boolean validateOrder(Order order) {
        logger.info("DigitalOrderProcessing: validation started (order ID: " + order.getOrderId() + ")");

        if (order.getItems() == null || order.getItems().isEmpty()) {
            logger.error("DigitalOrderProcessing: validation failed - order has no items");
            return false;
        }

        for (OrderItem item : order.getItems()) {
            if (item == null || item.getProduct() == null) {
                logger.error("DigitalOrderProcessing: validation failed - item/product is null");
                return false;
            }
            InputValidator.validateQuantity(item.getQuantity());
        }

        logger.info("DigitalOrderProcessing: validation passed (order ID: " + order.getOrderId() + ")");
        return true;
    }

    @Override
    protected double calculateTotal(Order order) {
        logger.info("DigitalOrderProcessing: processing started (calculate) (order ID: " + order.getOrderId() + ")");

        InvoicePolicy invoicePolicy = factory.createInvoicePolicy();
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            double subtotal = item.getProduct().getPrice() * item.getQuantity();
            double fees = invoicePolicy.calculateExtraFees(item.getProduct());
            total += subtotal + fees;
        }

        logger.info("DigitalOrderProcessing: processing calculated total=" + total + " (order ID: " + order.getOrderId() + ")");

        return total;
    }

    @Override
    protected boolean finalizeOrder(Order order) {
        if (order.isFinalized()) {
            logger.info("DigitalOrderProcessing: order already finalized - skipping finalizeOrder() (order ID: " + order.getOrderId() + ")");
            return true;
        }
        double finalizedTotal = orderService.finalizeOrder(order);
        logger.info("DigitalOrderProcessing: order finalized via OrderService (order ID: " + order.getOrderId() + ", returnedTotal=" + finalizedTotal + ")");
        return true;
    }

    @Override
    protected boolean handlePayment(Order order, PaymentProcessor processor) {
        logger.info("DigitalOrderProcessing: payment started (order ID: " + order.getOrderId() + ")");

        int paymentId = paymentDAO.getNextPaymentId();
        Payment payment = paymentService.processPayment(paymentId, order, processor);

        String status = payment != null ? payment.getStatus() : null;
        logger.info("DigitalOrderProcessing: payment finished status=" + status + " (order ID: " + order.getOrderId() + ")");

        return payment != null && "Approved".equals(payment.getStatus());
    }

    @Override
    protected boolean createShipment(Order order) {
        logger.info("DigitalOrderProcessing: shipment step skipped (digital order) (order ID: " + order.getOrderId() + ")");
        return true;
    }

    @Override
    protected void sendNotifications(Order order) {
        logger.info("DigitalOrderProcessing: notifications/events step completed via services (order ID: " + order.getOrderId() + ")");
    }
}

