package com.eoms.app.mediator;

import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.DAO.PaymentInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.entity.Order;
import com.eoms.config.Logger;
import com.eoms.factory.PaymentProcessor;
import com.eoms.template_method.DigitalOrderProcessing;
import com.eoms.template_method.OrderProcessingTemplate;
import com.eoms.template_method.PhysicalOrderProcessing;
import com.eoms.util.InputValidator;

/**
 * Implementation of OrderProcessingMediator that coordinates the order processing workflow.
 */
public class OrderProcessingMediatorImpl implements OrderProcessingMediator {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentInterface paymentDAO;
    private final ShipmentInterface shipmentDAO;
    private final Logger logger;

    public OrderProcessingMediatorImpl(OrderService orderService, PaymentService paymentService,
                                       PaymentInterface paymentDAO, ShipmentInterface shipmentDAO) {
        InputValidator.validateNotNull(orderService, "OrderService");
        InputValidator.validateNotNull(paymentService, "PaymentService");
        InputValidator.validateNotNull(paymentDAO, "PaymentDAO");
        InputValidator.validateNotNull(shipmentDAO, "ShipmentDAO");

        this.orderService = orderService;
        this.paymentService = paymentService;
        this.paymentDAO = paymentDAO;
        this.shipmentDAO = shipmentDAO;
        this.logger = Logger.getInstance();
    }

    @Override
    public boolean processOrder(Order order, PaymentProcessor processor) {
        InputValidator.validateNotNull(order, "Order");
        InputValidator.validateNotNull(processor, "PaymentProcessor");

        try {
            logger.info("OrderProcessingMediatorImpl: Selecting Template Method implementation (order ID: " + order.getOrderId() + ")");

            boolean isPhysicalOrder = order.getItems() != null
                    && order.getItems().stream().anyMatch(i -> i.getProduct() != null && i.getProduct().getType() == com.eoms.entity.Product.ProductType.PHYSICAL);

            OrderProcessingTemplate template = isPhysicalOrder
                    ? new PhysicalOrderProcessing(orderService, paymentService, paymentDAO, shipmentDAO)
                    : new DigitalOrderProcessing(orderService, paymentService, paymentDAO, shipmentDAO);

            logger.info("OrderProcessingMediatorImpl: Executing Template Method workflow (order ID: " + order.getOrderId() + ", type=" + (isPhysicalOrder ? "physical" : "digital") + ")");
            boolean success = template.processOrder(order, processor);
            logger.info("OrderProcessingMediatorImpl: Template Method workflow finished success=" + success + " (order ID: " + order.getOrderId() + ")");
            return success;

        } catch (Exception e) {
            logger.error("OrderProcessingMediatorImpl: Error during order processing: " + e.getMessage());
            order.setStatus("Failed");
            return false;
        }
    }
}