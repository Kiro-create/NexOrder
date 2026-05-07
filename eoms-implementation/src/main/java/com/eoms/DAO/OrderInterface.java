package com.eoms.DAO;

import com.eoms.entity.Order;

public interface OrderInterface {

    void saveOrder(Order order);

    Order findOrderById(int orderId);

    OrderIterator createIterator();

    int getNextOrderId();

}
