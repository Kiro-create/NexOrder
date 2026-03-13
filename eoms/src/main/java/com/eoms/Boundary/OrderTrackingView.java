package com.eoms.Boundary;

import java.util.Scanner;

<<<<<<< HEAD
import com.eoms.service.ShippingService;
=======
import com.eoms.Controller.TrackOrderStatusController;
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;

public class OrderTrackingView {

<<<<<<< HEAD
    private ShippingService shippingService;
    private Scanner scanner;

    public OrderTrackingView(ShippingService shippingService, Scanner scanner) {
        this.shippingService = shippingService;
=======
    private TrackOrderStatusController controller;
    private Scanner scanner;

    public OrderTrackingView(TrackOrderStatusController controller, Scanner scanner) {
        this.controller = controller;
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168
        this.scanner = scanner;
    }

    public void trackOrder() {

        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

<<<<<<< HEAD
        Order order = shippingService.getOrder(orderId);
=======
        Order order = controller.getOrder(orderId);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Order Status: " + order.getStatus());

<<<<<<< HEAD
        Shipment shipment = shippingService.getShipmentForOrder(orderId);
=======
        Shipment shipment = controller.getShipment(orderId);
>>>>>>> 16f1ee294919e802a56a462c14ee386ee6604168

        if (shipment != null) {
            System.out.println("Tracking Number: " + shipment.getTrackingNumber());
        }
    }
}