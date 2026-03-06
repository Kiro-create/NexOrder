package com.eoms.Controller;

import com.eoms.DAO.OrderInterface;
import com.eoms.DAO.ShipmentInterface;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;
import com.eoms.config.Logger;

public class TrackOrderStatusController {

    private OrderInterface orderDAO;
    private ShipmentInterface shipmentDAO;

    public TrackOrderStatusController(OrderInterface orderDAO,
                                      ShipmentInterface shipmentDAO) {

        this.orderDAO = orderDAO;
        this.shipmentDAO = shipmentDAO;
    }

    public Order getOrder(int orderId) {

        Logger logger = Logger.getInstance();

        logger.info("Retrieving order with ID: " + orderId);

        return orderDAO.findOrderById(orderId);
    }

    public Shipment getShipment(int orderId) {

        Logger logger = Logger.getInstance();

        logger.info("Retrieving shipment for order ID: " + orderId);

        return shipmentDAO.findShipmentByOrder(orderId);
    }

}