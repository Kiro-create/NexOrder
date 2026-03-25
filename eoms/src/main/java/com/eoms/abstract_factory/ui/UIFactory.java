package com.eoms.abstract_factory.ui;

public interface UIFactory {
    Menu createMenu();

    Dashboard createDashboard();

    Form createForm();
}
