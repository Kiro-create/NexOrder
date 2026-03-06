package com.eoms.DAO;

import com.eoms.entity.Shipment;

public interface ShipmentInterface {

    void saveShipment(Shipment shipment);

    Shipment findShipmentByOrder(int orderId);

}