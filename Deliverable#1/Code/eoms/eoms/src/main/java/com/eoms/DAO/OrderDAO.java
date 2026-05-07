package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Order;

public class OrderDAO implements OrderInterface {

    private List<Order> orders = new ArrayList<>();

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

}