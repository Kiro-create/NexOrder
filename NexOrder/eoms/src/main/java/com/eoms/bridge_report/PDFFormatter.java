package com.eoms.bridge_report;

import com.eoms.config.Logger;

public class PDFFormatter implements ReportFormatter {
    @Override
    public void format(String content) {
        Logger.getInstance().info("PDF Report: " + content);
    }
}