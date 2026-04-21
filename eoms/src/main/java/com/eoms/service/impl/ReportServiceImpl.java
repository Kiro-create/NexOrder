package com.eoms.service.impl;


import com.eoms.config.Logger;
import com.eoms.entity.Product;
import com.eoms.service.ProductService;
import com.eoms.service.ReportService;
import com.eoms.template_method.InventoryReportGeneration;
import com.eoms.template_method.ReportGenerationTemplate;
import com.eoms.template_method.SalesReportGeneration;


import java.util.List;
import java.util.Map;

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

        Logger.getInstance().info("ReportServiceImpl: Selecting Template Method implementation (reportChoice=" + reportChoice + ")");

        String reportType = (reportChoice == SALES) ? "sales" : "inventory";
        ReportGenerationTemplate template = (reportChoice == SALES)
                ? new SalesReportGeneration()
                : new InventoryReportGeneration();

        List<Product> products = productService.getAllProducts();

        Map<String, Object> parameters = Map.of(
                "formatChoice", formatChoice,
                "products", products
        );

        Logger.getInstance().info("ReportServiceImpl: Executing Template Method workflow (type=" + reportType + ")");
        boolean ok = template.generateReport(reportType, parameters);
        Logger.getInstance().info("ReportServiceImpl: Template Method workflow finished success=" + ok + " (type=" + reportType + ")");
        
    }
}
