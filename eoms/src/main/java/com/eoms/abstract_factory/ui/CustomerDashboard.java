package com.eoms.abstract_factory.ui;

public class CustomerDashboard extends AbstractDashboard {

    @Override
    public void showDashboard() {
        showSection(
                "Customer Dashboard",
                "Last order status: Shipped\n"
                        + "Saved addresses: 2\n"
                        + "Recommended products: 4");
    }
}
