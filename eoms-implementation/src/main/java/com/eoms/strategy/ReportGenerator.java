package com.eoms.strategy;

public class ReportGenerator {

    private ReportFormattingStrategy strategy;

    public void setStrategy(ReportFormattingStrategy strategy) {
        this.strategy = strategy;
    }

    public void generate(String data) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set");
        }
        strategy.format(data);
    }
}