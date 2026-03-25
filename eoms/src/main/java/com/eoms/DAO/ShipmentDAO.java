package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Shipment;

public class ShipmentDAO implements ShipmentInterface {

    private List<Shipment> shipments = new ArrayList<>();

    @Override
    public void saveShipment(Shipment shipment) {

        shipments.add(shipment);

        System.out.println("Shipment created with tracking number: "
                + shipment.getTrackingNumber());
    }

    @Override
    public Shipment findShipmentByOrder(int orderId) {

        for (Shipment s : shipments) {

            if (s != null && s.getOrderId() == orderId) {
                return s;
            }

        }

        return null;
    }

}