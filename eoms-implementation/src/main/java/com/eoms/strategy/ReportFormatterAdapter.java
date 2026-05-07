package com.eoms.strategy;

import com.eoms.bridge_report.Report;

public class ReportFormatterAdapter implements ReportFormattingStrategy {

    private Report report;

    public ReportFormatterAdapter(Report report) {
        this.report = report;
    }

    @Override
    public void format(String data) {
        report.generate(data);
    }
}