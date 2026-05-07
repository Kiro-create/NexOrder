package com.eoms.app;
import com.eoms.bridge_report.Report;
import com.eoms.bridge_report.ReportFormatter;
import com.eoms.bridge_report.PDFFormatter;
import com.eoms.bridge_report.ExcelFormatter;
import com.eoms.bridge_report.SalesReport;
import com.eoms.bridge_report.InventoryReport;
import com.eoms.strategy.ReportFormatterAdapter;
import com.eoms.strategy.ReportFormattingStrategy;

public class ReportStrategyProvider {

    private static final int SALES = 1;
    private static final int INVENTORY = 2;
    private static final int PDF = 1;
    private static final int EXCEL = 2;

    public static ReportFormattingStrategy getStrategy(int reportChoice, int formatChoice) {

        // choose formatter (Bridge)
        ReportFormatter formatter = (formatChoice == PDF)
                ? new PDFFormatter()
                : new ExcelFormatter();

        // choose report type (Bridge)
        Report report = (reportChoice == SALES)
                ? new SalesReport(formatter)
                : new InventoryReport(formatter);

        // wrap with Strategy (Adapter)
        return new ReportFormatterAdapter(report);
    }
}