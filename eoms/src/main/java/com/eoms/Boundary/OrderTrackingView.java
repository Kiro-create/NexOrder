package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.Controller.TrackOrderStatusController;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;

public class OrderTrackingView {

    private TrackOrderStatusController controller;
    private Scanner scanner;

    public OrderTrackingView(TrackOrderStatusController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void trackOrder() {

        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = controller.getOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Order Status: " + order.getStatus());

        Shipment shipment = controller.getShipment(orderId);

        if (shipment != null) {
            System.out.println("Tracking Number: " + shipment.getTrackingNumber());
        }
    }
}