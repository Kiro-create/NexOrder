package com.eoms.bridge_report;
public class SalesReport extends Report {

    public SalesReport(ReportFormatter formatter) {
        super(formatter);
    }

  @Override
public void generate(String data) {
    formatter.format("Sales Report:\n" + data);
}
}