package com.eoms.abstract_factory.ui;

public class AdminMenu extends AbstractMenu {

    @Override
    public void showMenu() {
        showSection(
                "ADMIN PANEL",
                "1. Add Product\n"
                        + "2. View Products\n"
                        + "0. Back");
    }
}
