package com.eoms.adapter;

import com.eoms.service.ShippingService;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;
import com.eoms.externalservice.DHLApi;

public class DHLShippingAdapter implements ShippingService {

    private DHLApi dhl = new DHLApi();

    @Override
    public Order getOrder(int orderId) {
        return dhl.fetchOrderData(orderId);
    }

    @Override
    public Shipment getShipmentForOrder(int orderId) {
        return dhl.trackShipment(orderId);
    }
}