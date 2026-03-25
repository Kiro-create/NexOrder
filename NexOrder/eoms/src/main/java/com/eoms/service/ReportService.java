package com.eoms.service;

/**
 * Application / business use-case for exporting catalog data as a report.
 * Decouples presentation from {@link com.eoms.bridge_report} and from DAO details (DIP).
 */
public interface ReportService {

    /**
     * @param reportChoice 1 = sales (prices), 2 = inventory (stock)
     * @param formatChoice 1 = PDF-style output, 2 = Excel-style output
     */
    void generateReport(int reportChoice, int formatChoice);
}
