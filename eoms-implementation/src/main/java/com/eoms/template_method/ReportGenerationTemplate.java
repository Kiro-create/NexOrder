package com.eoms.template_method;

import com.eoms.config.Logger;
import com.eoms.util.InputValidator;
import java.util.Map;

/**
 * Template Method pattern for report generation workflow.
 * Defines the skeleton of the report generation algorithm.
 */
public abstract class ReportGenerationTemplate {

    protected final Logger logger = Logger.getInstance();

    /**
     * Template method: Defines the invariant report generation workflow.
     *
     * Steps (with required logging):
     * - validation
     * - data collection
     * - formatting
     * - output
     */
    public final boolean generateReport(String reportType, Map<String, Object> parameters) {
        InputValidator.validateNotNull(reportType, "Report type");
        InputValidator.validateNotNull(parameters, "Parameters");

        logger.info("ReportGenerationTemplate: Starting workflow type=" + reportType);

        try {
            logger.info("ReportGenerationTemplate: Step=validation type=" + reportType);
            if (!validateParameters(parameters)) {
                logger.error("ReportGenerationTemplate: Validation failed type=" + reportType);
                return false;
            }

            logger.info("ReportGenerationTemplate: Step=collectData type=" + reportType);
            Object data = collectData(parameters);
            if (data == null) {
                logger.error("ReportGenerationTemplate: Data collection failed type=" + reportType);
                return false;
            }

            logger.info("ReportGenerationTemplate: Step=formatting type=" + reportType);
            Object formattedData = formatData(data, parameters);
            if (formattedData == null) {
                logger.error("ReportGenerationTemplate: Formatting failed type=" + reportType);
                return false;
            }

            logger.info("ReportGenerationTemplate: Step=output type=" + reportType);
            if (!outputReport(formattedData)) {
                logger.error("ReportGenerationTemplate: Output failed type=" + reportType);
                return false;
            }

            logger.info("ReportGenerationTemplate: Workflow completed successfully type=" + reportType);
            return true;
        } catch (Exception e) {
            logger.error("ReportGenerationTemplate: Unexpected error type=" + reportType + " reason=" + e.getMessage());
            return false;
        }
    }

    protected abstract boolean validateParameters(Map<String, Object> parameters);

    protected abstract Object collectData(Map<String, Object> parameters);

    /**
     * Formatting step should use the existing Strategy for formatting.
     * Parameters are provided so implementations can select Strategy.
     */
    protected abstract Object formatData(Object data, Map<String, Object> parameters);

    /**
     * Output step should use the existing Bridge (through the existing adapter/strategy setup).
     */
    protected abstract boolean outputReport(Object formattedData);
}

