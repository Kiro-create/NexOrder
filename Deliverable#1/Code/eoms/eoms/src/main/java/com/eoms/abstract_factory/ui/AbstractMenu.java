package com.eoms.abstract_factory.ui;

public abstract class AbstractMenu implements Menu {

    protected void showSection(String title, String content) {
        System.out.println("=== " + title + " ===");
        System.out.println(content);
        System.out.println();
    }
}
