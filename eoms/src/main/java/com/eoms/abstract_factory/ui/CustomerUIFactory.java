package com.eoms.abstract_factory.ui;

public class CustomerUIFactory implements UIFactory {

    @Override
    public Menu createMenu() {
        return new CustomerMenu();
    }

    @Override
    public Dashboard createDashboard() {
        return new CustomerDashboard();
    }

    @Override
    public Form createForm() {
        return new CustomerOrderForm();
    }
}
