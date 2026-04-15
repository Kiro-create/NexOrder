package com.eoms.abstract_factory.ui;

public class CustomerMenu extends AbstractMenu {

    @Override
    public void showMenu() {
        showSection(
                "CUSTOMER PANEL",
                "1. Browse Products\n"
                        + "2. Create Order\n"
                        + "3. Add Product to Cart\n"
                        + "4. Complete Order\n"
                        + "5. Track Order\n"
                        + "0. Back");
    }
}
