package com.eoms.template_method;

import com.eoms.app.ReportStrategyProvider;
import com.eoms.config.Logger;
import com.eoms.entity.Product;
import com.eoms.strategy.ReportFormattingStrategy;
import com.eoms.strategy.ReportGenerator;
import com.eoms.util.InputValidator;
import java.util.List;
import java.util.Map;

/**
 * Concrete Template Method implementation for sales reports.
 * Uses Strategy (formatting) + Bridge (output) through {@link ReportStrategyProvider}.
 */
public class SalesReportGeneration extends ReportGenerationTemplate {

    private static final int SALES = 1;
    private static final int PDF = 1;
    private static final int EXCEL = 2;

    private final Logger log = Logger.getInstance();

    @Override
    protected boolean validateParameters(Map<String, Object> parameters) {
        log.info("SalesReportGeneration: validation started");
        if (!parameters.containsKey("formatChoice")) {
            log.error("SalesReportGeneration: Missing parameter formatChoice");
            return false;
        }
        int formatChoice = (int) parameters.get("formatChoice");
        if (formatChoice != PDF && formatChoice != EXCEL) {
            log.error("SalesReportGeneration: Invalid formatChoice=" + formatChoice);
            return false;
        }
        log.info("SalesReportGeneration: validation passed");
        return true;
    }

    @Override
    protected Object collectData(Map<String, Object> parameters) {
        log.info("SalesReportGeneration: collectData started");
        Object productsObj = parameters.get("products");
        if (!(productsObj instanceof List)) {
            log.error("SalesReportGeneration: products parameter missing/invalid");
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Product> products = (List<Product>) productsObj;

        int formatChoice = (int) parameters.get("formatChoice");
        String data = buildData(products, SALES, formatChoice);
        log.info("SalesReportGeneration: collectData completed (chars=" + data.length() + ")");
        return data;
    }

    @Override
    protected Object formatData(Object data, Map<String, Object> parameters) {
        log.info("SalesReportGeneration: formatting started");
        InputValidator.validateNotNull(data, "data");
        if (!(data instanceof String)) {
            log.error("SalesReportGeneration: Invalid data type for formatting");
            return null;
        }
        int formatChoice = (int) parameters.get("formatChoice");
        ReportFormattingStrategy strategy = ReportStrategyProvider.getStrategy(SALES, formatChoice);
        log.info("SalesReportGeneration: formatting strategy selected");
        return new FormattedPayload((String) data, strategy);
    }

    @Override
    protected boolean outputReport(Object formattedData) {
        log.info("SalesReportGeneration: output started");
        if (!(formattedData instanceof FormattedPayload)) {
            log.error("SalesReportGeneration: Invalid formatted payload type");
            return false;
        }
        FormattedPayload payload = (FormattedPayload) formattedData;

        // Strategy context
        ReportGenerator generator = new ReportGenerator();
        generator.setStrategy(payload.strategy);
        generator.generate(payload.data);

        log.info("SalesReportGeneration: output completed");
        return true;
    }

    private static String buildData(List<Product> products, int reportChoice, int formatChoice) {
        if (products == null || products.isEmpty()) {
            return formatChoice == EXCEL ? "No products available" : "Products:\n\nNo products available\n";
        }
        if (formatChoice == EXCEL) {
            return buildExcelRows(products, reportChoice);
        }
        return buildPdfLayout(products, reportChoice);
    }

    private static String buildExcelRows(List<Product> products, int reportChoice) {
        StringBuilder data = new StringBuilder();
        data.append("Product|Price\n");
        for (Product product : products) {
            data.append(product.getName()).append('|').append(product.getPrice()).append('\n');
        }
        return data.toString();
    }

    private static String buildPdfLayout(List<Product> products, int reportChoice) {
        StringBuilder data = new StringBuilder("Products:\n\n");
        for (Product product : products) {
            data.append(product.getName()).append('\n');
            data.append("Price: ").append(product.getPrice());
            data.append("\n\n");
        }
        return data.toString();
    }
}

