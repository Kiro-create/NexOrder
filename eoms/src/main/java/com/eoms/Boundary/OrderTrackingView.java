package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.service.ShippingService;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;

public class OrderTrackingView {

    private ShippingService shippingService;
    private Scanner scanner;

    public OrderTrackingView(ShippingService shippingService, Scanner scanner) {
        this.shippingService = shippingService;
        this.scanner = scanner;
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
            System.out.println("Tracking Number: " + shipment.getTrackingNumber());
        }
    }
}