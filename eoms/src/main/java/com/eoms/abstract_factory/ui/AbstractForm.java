package com.eoms.abstract_factory.ui;

public abstract class AbstractForm implements Form {

    protected void showSection(String title, String content) {
        System.out.println("=== " + title + " ===");
        System.out.println(content);
        System.out.println();
    }
}
