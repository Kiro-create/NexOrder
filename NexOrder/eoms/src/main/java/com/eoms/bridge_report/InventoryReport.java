package com.eoms.bridge_report;

public class InventoryReport extends Report {

    public InventoryReport(ReportFormatter formatter) {
        super(formatter);
    }

    @Override
public void generate(String data) {
    formatter.format("Inventory Report:\n" + data);
}
}