package com.eoms.DAO;

import java.util.*;
import com.eoms.entity.Order;
import com.eoms.config.Logger;

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
        // Intentionally implemented via Iterator pattern (client uses OrderDAO through this API).
        Logger.getInstance().info("OrderDAO: findOrderById using OrderIterator (orderId=" + orderId + ")");
        System.out.println("[INFO] OrderDAO: iterating orders via OrderIterator (orderId=" + orderId + ")");
        OrderIterator iterator = createIterator();
        while (iterator.hasNext()) {
            Order o = iterator.next();
            if (o.getOrderId() == orderId) {
                return o;
            }
        }
        return null;
    }

    @Override
    public OrderIterator createIterator() {
        // Snapshot to keep iteration stable even if orders change elsewhere.
        return new OrderListIterator(Collections.unmodifiableList(new ArrayList<>(orders)));
    }

    @Override
    public int getNextOrderId() {
        return nextOrderId++;
    }

}
