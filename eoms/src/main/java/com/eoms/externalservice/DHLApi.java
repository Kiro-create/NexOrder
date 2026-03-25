package com.eoms.externalservice;

import com.eoms.entity.Customer;
import com.eoms.entity.Order;
import com.eoms.entity.Shipment;

public class DHLApi {

	public Order fetchOrderData(int id){
	    System.out.println("DHL fetching order " + id);
	    Customer customer = new Customer(0, "External Customer", "external@email.com");
	    Order order = new Order(id,customer);
	    order.setStatus("Shipped");

	    return order;
	}
	public Shipment trackShipment(int id){
	    System.out.println("DHL tracking shipment " + id);

	    Shipment shipment = new Shipment(id, id, "TRK-" + id);
	    shipment.setStatus("In Transit");

	    return shipment;
	}
}