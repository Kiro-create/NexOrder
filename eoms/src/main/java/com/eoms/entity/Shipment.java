package com.eoms.entity;

public class Shipment {

    private int shipmentId;
    private int orderId;
    private String trackingNumber;
    private String status;


    public Shipment(int shipmentId, int orderId, String trackingNumber) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getOrderId() {
        return orderId;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}