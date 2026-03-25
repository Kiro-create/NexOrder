package com.eoms.service;

import com.eoms.entity.Order;
import com.eoms.entity.Shipment;

public interface ShippingService {
    Order getOrder(int orderId);
    Shipment getShipmentForOrder(int orderId);
}
