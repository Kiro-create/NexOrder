package com.eoms.abstract_factory.ui;

public class CustomerOrderForm extends AbstractForm {

    @Override
    public void showForm() {
        showSection(
                "Customer Order Form",
                "Fields: Shipping Address, Phone Number, Payment Method");
    }
}
