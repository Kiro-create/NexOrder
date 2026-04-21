package com.eoms.DAO;

import java.util.List;
import java.util.NoSuchElementException;
import com.eoms.entity.Order;

public class OrderListIterator implements OrderIterator {

    private final List<Order> orders;
    private int position = 0;

    public OrderListIterator(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean hasNext() {
        return position < orders.size();
    }

    @Override
    public Order next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more orders");
        }
        return orders.get(position++);
    }

}
