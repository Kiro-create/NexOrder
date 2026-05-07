package com.eoms.abstract_factory.ui;

public class AdminDashboard extends AbstractDashboard {

    @Override
    public void showDashboard() {
        showSection(
                "Admin Dashboard",
                "Orders pending review: 12\n"
                        + "Products low in stock: 5\n"
                        + "Active support tickets: 3");
    }
}
