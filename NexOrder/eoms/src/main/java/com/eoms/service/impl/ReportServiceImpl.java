package com.eoms.service.impl;

import com.eoms.bridge_report.ExcelFormatter;
import com.eoms.bridge_report.InventoryReport;
import com.eoms.bridge_report.PDFFormatter;
import com.eoms.bridge_report.Report;
import com.eoms.bridge_report.ReportFormatter;
import com.eoms.bridge_report.SalesReport;
import com.eoms.config.Logger;
import com.eoms.entity.Product;
import com.eoms.service.ProductService;
import com.eoms.service.ReportService;

import java.util.List;

public class ReportServiceImpl implements ReportService {

    private static final int SALES = 1;
    private static final int INVENTORY = 2;
    private static final int PDF = 1;
    private static final int EXCEL = 2;

    private final ProductService productService;

    public ReportServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void generateReport(int reportChoice, int formatChoice) {
        if (reportChoice != SALES && reportChoice != INVENTORY) {
            Logger.getInstance().error("Invalid report type: " + reportChoice);
            return;
        }
        if (formatChoice != PDF && formatChoice != EXCEL) {
            Logger.getInstance().error("Invalid export format: " + formatChoice);
            return;
        }

        String data = buildData(productService.getAllProducts(), reportChoice, formatChoice);
        ReportFormatter formatter = formatChoice == PDF ? new PDFFormatter() : new ExcelFormatter();
        Report report = reportChoice == SALES ? new SalesReport(formatter) : new InventoryReport(formatter);
        report.generate(data);
    }

    private static String buildData(List<Product> products, int reportChoice, int formatChoice) {
        if (products.isEmpty()) {
            return formatChoice == EXCEL ? "No products available" : "Products:\n\nNo products available\n";
        }
        if (formatChoice == EXCEL) {
            return buildExcelRows(products, reportChoice);
        }
        return buildPdfLayout(products, reportChoice);
    }

    /** One row per product; columns separated by | */
    private static String buildExcelRows(List<Product> products, int reportChoice) {
        StringBuilder data = new StringBuilder();
        if (reportChoice == SALES) {
            data.append("Product|Price\n");
            for (Product product : products) {
                data.append(product.getName()).append('|').append(product.getPrice()).append('\n');
            }
        } else {
            data.append("Product|Stock\n");
            for (Product product : products) {
                data.append(product.getName()).append('|').append(product.getStock()).append('\n');
            }
        }
        return data.toString();
    }

    /** Each value on its own line; blank line between products */
    private static String buildPdfLayout(List<Product> products, int reportChoice) {
        StringBuilder data = new StringBuilder("Products:\n\n");
        for (Product product : products) {
            data.append(product.getName()).append('\n');
            if (reportChoice == SALES) {
                data.append("Price: ").append(product.getPrice());
            } else {
                data.append("Stock: ").append(product.getStock());
            }
            data.append("\n\n");
        }
        return data.toString();
    }
}
