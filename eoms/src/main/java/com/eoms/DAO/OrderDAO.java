package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Order;

public class OrderDAO implements OrderInterface {

    private List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    @Override
    public void saveOrder(Order order) {

        orders.add(order);

        System.out.println("Order saved with ID: " + order.getOrderId());
    }

    @Override
    public Order findOrderById(int orderId) {

        for (Order o : orders) {

            if (o.getOrderId() == orderId) {
                return o;
            }

        }

        return null;
    }

    @Override
    public int getNextOrderId() {
        return nextOrderId++;
    }

}