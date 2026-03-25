package com.eoms.service.impl;

import com.eoms.service.ShippingService;
import com.eoms.DAO.OrderInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;
import com.eoms.config.Logger;

public class ShippingServiceImpl implements ShippingService {
    private final OrderInterface orderDAO;
    private final ShipmentInterface shipmentDAO;

    public ShippingServiceImpl(OrderInterface orderDAO, ShipmentInterface shipmentDAO) {
        if (orderDAO == null || shipmentDAO == null) {
            throw new IllegalArgumentException("DAOs must not be null");
        }
        this.orderDAO = orderDAO;
        this.shipmentDAO = shipmentDAO;
    }

    @Override
    public Order getOrder(int orderId) {
        Logger logger = Logger.getInstance();
        logger.info("Retrieving order with ID: " + orderId);
        return orderDAO.findOrderById(orderId);
    }

    @Override
    public Shipment getShipmentForOrder(int orderId) {
        Logger logger = Logger.getInstance();
        logger.info("Retrieving shipment for order ID: " + orderId);
        return shipmentDAO.findShipmentByOrder(orderId);
    }
}
