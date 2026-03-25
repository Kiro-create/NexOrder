package com.eoms.Boundary;

import com.eoms.service.ReportService;

import java.util.Scanner;

/**
 * Presentation layer: collects report options from the admin and delegates generation to
 * {@link ReportService} (no direct DAO access).
 */
public class AdminReportView {

    private final ReportService reportService;
    private final Scanner scanner;

    public AdminReportView(ReportService reportService, Scanner scanner) {
        this.reportService = reportService;
        this.scanner = scanner;
    }

    public void promptAndGenerate() {
        System.out.println("Choose Report Type:");
        System.out.println("1. Sales");
        System.out.println("2. Inventory");

        int reportChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Choose Format:");
        System.out.println("1. PDF");
        System.out.println("2. Excel");

        int formatChoice = scanner.nextInt();
        scanner.nextLine();

        reportService.generateReport(reportChoice, formatChoice);
    }
}
