package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.bridge_notification.Notification;
import com.eoms.service.ShippingService;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;

public class OrderTrackingView {

    private ShippingService shippingService;
    private Scanner scanner;
    private final Notification shippingUpdateNotification;

    public OrderTrackingView(
            ShippingService shippingService,
            Scanner scanner,
            Notification shippingUpdateNotification) {
        this.shippingService = shippingService;
        this.scanner = scanner;
        this.shippingUpdateNotification = shippingUpdateNotification;
    }

    public void trackOrder() {

        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = shippingService.getOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Order Status: " + order.getStatus());

        Shipment shipment = shippingService.getShipmentForOrder(orderId);

        if (shipment != null) {
            System.out.println("Shipment status: " + shipment.getStatus());
            System.out.println("Tracking Number: " + shipment.getTrackingNumber());
            shippingUpdateNotification.send(
                    "Order " + orderId + " | " + shipment.getStatus() + " | " + shipment.getTrackingNumber());
        }
    }
}