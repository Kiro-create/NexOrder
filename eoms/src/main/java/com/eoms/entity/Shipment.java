package com.eoms.entity;

public class Shipment {

    private int shipmentId;
    private int orderId;
    private String trackingNumber;

    public Shipment(int shipmentId, int orderId, String trackingNumber) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

}