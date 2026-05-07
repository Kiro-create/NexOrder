package com.eoms.abstract_factory.ui;

public class ProductManagementForm extends AbstractForm {

    @Override
    public void showForm() {
        showSection(
                "Product Management Form",
                "Fields: Product Name, SKU, Price, Quantity, Category");
    }
}
