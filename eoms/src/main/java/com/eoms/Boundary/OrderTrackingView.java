package com.eoms.Boundary;

import java.util.Scanner;

import com.eoms.app.mediator.CustomerMediator;
import com.eoms.bridge_notification.Notification;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;
import com.eoms.util.InputValidator;

public class OrderTrackingView {

    private CustomerMediator mediator;
    private Scanner scanner;
    private final Notification shippingUpdateNotification;

    public OrderTrackingView(
            CustomerMediator mediator,
            Scanner scanner,
            Notification shippingUpdateNotification) {
        this.mediator = mediator;
        this.scanner = scanner;
        this.shippingUpdateNotification = shippingUpdateNotification;
    }

    public void trackOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = scanner.nextInt();
            InputValidator.validatePositiveInt(orderId, "Order ID");
            scanner.nextLine();

            Order order = mediator.getOrderById(orderId);

            if (order == null) {
                System.out.println("Order not found.");
                return;
            }

            System.out.println("Order Status: " + order.getStatus());

            Shipment shipment = mediator.getShipmentForOrder(orderId);

            if (shipment != null) {
                System.out.println("Shipment status: " + shipment.getStatus());
                System.out.println("Tracking Number: " + shipment.getTrackingNumber());
                shippingUpdateNotification.send(
                        "Order " + orderId + " | " + shipment.getStatus() + " | " + shipment.getTrackingNumber());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}