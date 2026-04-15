package com.eoms.service.impl;

import com.eoms.service.OrderProcessingMediator;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.service.ShippingService;
import com.eoms.observer.OrderEvent;
import com.eoms.observer.OrderEventManager;
import com.eoms.observer.OrderEventType;
import com.eoms.DAO.PaymentInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.entity.Order;
import com.eoms.entity.Payment;
import com.eoms.entity.Shipment;
import com.eoms.config.Logger;
import com.eoms.factory.PaymentProcessor;
import com.eoms.util.InputValidator;

/**
 * Implementation of OrderProcessingMediator that coordinates the order processing workflow.
 */
public class OrderProcessingMediatorImpl implements OrderProcessingMediator {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final PaymentInterface paymentDAO;
    private final ShipmentInterface shipmentDAO;
    private final Logger logger;

    public OrderProcessingMediatorImpl(OrderService orderService, PaymentService paymentService,
                                       ShippingService shippingService, PaymentInterface paymentDAO,
                                       ShipmentInterface shipmentDAO) {
        InputValidator.validateNotNull(orderService, "OrderService");
        InputValidator.validateNotNull(paymentService, "PaymentService");
        InputValidator.validateNotNull(shippingService, "ShippingService");
        InputValidator.validateNotNull(paymentDAO, "PaymentDAO");
        InputValidator.validateNotNull(shipmentDAO, "ShipmentDAO");

        this.orderService = orderService;
        this.paymentService = paymentService;
        this.shippingService = shippingService;
        this.paymentDAO = paymentDAO;
        this.shipmentDAO = shipmentDAO;
        this.logger = Logger.getInstance();
    }

    @Override
    public boolean processOrder(Order order, PaymentProcessor processor) {
        InputValidator.validateNotNull(order, "Order");
        InputValidator.validateNotNull(processor, "PaymentProcessor");

        try {
            // Step 1: Finalize the order
            double total = orderService.finalizeOrder(order);

            // Step 2: Process payment
            int paymentId = paymentDAO.getNextPaymentId();
            Payment payment = paymentService.processPayment(paymentId, order, processor);

            if (!"Approved".equals(payment.getStatus())) {
                logger.error("OrderProcessingMediatorImpl: Payment failed for order ID: " + order.getOrderId());
                return false;
            }

            // Step 3: Create shipment
            int shipmentId = shipmentDAO.getNextShipmentId();
            String trackingNumber = "TRK" + shipmentId;
            Shipment shipment = new Shipment(shipmentId, order.getOrderId(), trackingNumber);
            shipment.setStatus("Shipped");
            shipmentDAO.saveShipment(shipment);
            order.setStatus("Shipped");

            return true;

        } catch (Exception e) {
            logger.error("OrderProcessingMediatorImpl: Error during order processing: " + e.getMessage());
            order.setStatus("Failed");
            return false;
        }
    }
}