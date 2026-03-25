package com.eoms.bridge_report;

import com.eoms.config.Logger;

public class ExcelFormatter implements ReportFormatter {
    @Override
    public void format(String content) {
        Logger.getInstance().info("Excel Report: " + content);
    }
}