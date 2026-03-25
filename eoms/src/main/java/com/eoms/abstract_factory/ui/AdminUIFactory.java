package com.eoms.abstract_factory.ui;

public class AdminUIFactory implements UIFactory {

    @Override
    public Menu createMenu() {
        return new AdminMenu();
    }

    @Override
    public Dashboard createDashboard() {
        return new AdminDashboard();
    }

    @Override
    public Form createForm() {
        return new ProductManagementForm();
    }
}
