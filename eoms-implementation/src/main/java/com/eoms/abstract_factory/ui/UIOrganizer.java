package com.eoms.abstract_factory.ui;

public class UIOrganizer {

    private final Menu menu;
    private final Dashboard dashboard;
    private final Form form;

    public UIOrganizer(UIFactory factory) {
        this.menu = factory.createMenu();
        this.dashboard = factory.createDashboard();
        this.form = factory.createForm();
    }

    public void displayUI() {
        menu.showMenu();
        dashboard.showDashboard();
        form.showForm();
    }
}
