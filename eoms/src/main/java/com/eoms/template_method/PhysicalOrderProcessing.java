package com.eoms.template_method;

import com.eoms.DAO.PaymentInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.abstract_factory.InvoicePolicy;
import com.eoms.abstract_factory.PhysicalProductFactory;
import com.eoms.entity.Order;
import com.eoms.entity.OrderItem;
import com.eoms.entity.Payment;
import com.eoms.entity.Shipment;
import com.eoms.factory.PaymentProcessor;
import com.eoms.service.OrderService;
import com.eoms.service.PaymentService;
import com.eoms.util.InputValidator;

/**
 * Concrete Template Method implementation for physical product orders.
 * Integrates with existing Abstract Factory (stock/invoice), Strategy (payment), Observer (via services), and Mediator (caller).
 */
public class PhysicalOrderProcessing extends OrderProcessingTemplate {

    private final PhysicalProductFactory factory = new PhysicalProductFactory();
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentInterface paymentDAO;
    private final ShipmentInterface shipmentDAO;

    public PhysicalOrderProcessing(
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
        logger.info("PhysicalOrderProcessing: validation started (order ID: " + order.getOrderId() + ")");

        if (order.getItems() == null || order.getItems().isEmpty()) {
            logger.error("PhysicalOrderProcessing: validation failed - order has no items");
            return false;
        }

        // Stock is already reserved when items are added to the order (InventoryManager.reserveStock).
        // Validating via PhysicalStockHandler here would decrease stock a second time.
        for (OrderItem item : order.getItems()) {
            if (item == null || item.getProduct() == null) {
                logger.error("PhysicalOrderProcessing: validation failed - item/product is null");
                return false;
            }
            InputValidator.validateQuantity(item.getQuantity());
            // Basic sanity check: stock should never be negative after reservation.
            if (item.getProduct().getStock() < 0) {
                logger.error("PhysicalOrderProcessing: validation failed - stock is negative for product="
                        + item.getProduct().getName());
                return false;
            }
        }

        logger.info("PhysicalOrderProcessing: validation passed (order ID: " + order.getOrderId() + ")");
        return true;
    }

    @Override
    protected double calculateTotal(Order order) {
        logger.info("PhysicalOrderProcessing: processing started (calculate) (order ID: " + order.getOrderId() + ")");

        // Abstract Factory: apply invoice policy extra fees per item
        InvoicePolicy invoicePolicy = factory.createInvoicePolicy();
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            double subtotal = item.getProduct().getPrice() * item.getQuantity();
            double fees = invoicePolicy.calculateExtraFees(item.getProduct());
            total += subtotal + fees;
        }

        logger.info("PhysicalOrderProcessing: processing calculated total=" + total + " (order ID: " + order.getOrderId() + ")");

        return total;
    }

    @Override
    protected boolean finalizeOrder(Order order) {
        // Persist/finalize through service (Observer events published here)
        if (order.isFinalized()) {
            logger.info("PhysicalOrderProcessing: order already finalized - skipping finalizeOrder() (order ID: " + order.getOrderId() + ")");
            return true;
        }
        double finalizedTotal = orderService.finalizeOrder(order);
        logger.info("PhysicalOrderProcessing: order finalized via OrderService (order ID: " + order.getOrderId() + ", returnedTotal=" + finalizedTotal + ")");
        return true;
    }

    @Override
    protected boolean handlePayment(Order order, PaymentProcessor processor) {
        logger.info("PhysicalOrderProcessing: payment started (order ID: " + order.getOrderId() + ")");

        int paymentId = paymentDAO.getNextPaymentId();
        Payment payment = paymentService.processPayment(paymentId, order, processor);

        String status = payment != null ? payment.getStatus() : null;
        logger.info("PhysicalOrderProcessing: payment finished status=" + status + " (order ID: " + order.getOrderId() + ")");

        return payment != null && "Approved".equals(payment.getStatus());
    }

    @Override
    protected boolean createShipment(Order order) {
        logger.info("PhysicalOrderProcessing: shipment started (order ID: " + order.getOrderId() + ")");

        int shipmentId = shipmentDAO.getNextShipmentId();
        String trackingNumber = "TRK" + shipmentId;

        Shipment shipment = new Shipment(shipmentId, order.getOrderId(), trackingNumber);
        shipment.setStatus("Shipped");
        shipmentDAO.saveShipment(shipment);

        order.setStatus("Shipped");

        logger.info("PhysicalOrderProcessing: shipment saved (shipmentId=" + shipmentId + ", tracking=" + trackingNumber
                + ") (order ID: " + order.getOrderId() + ")");
        return true;
    }

    @Override
    protected void sendNotifications(Order order) {
        // Observer is already used by OrderService (ORDER_FINALIZED) and PaymentService (ORDER_PAID).
        logger.info("PhysicalOrderProcessing: notifications/events step completed via services (order ID: " + order.getOrderId() + ")");
    }
}

