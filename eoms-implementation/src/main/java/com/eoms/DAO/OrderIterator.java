package com.eoms.DAO;

import com.eoms.entity.Order;

public interface OrderIterator {

    boolean hasNext();

    Order next();

}
