package com.eoms.template_method;

import com.eoms.strategy.ReportFormattingStrategy;
import com.eoms.util.InputValidator;

/**
 * Internal data holder used by report template implementations to carry
 * raw report data together with the selected formatting strategy.
 */
final class FormattedPayload {
    final String data;
    final ReportFormattingStrategy strategy;

    FormattedPayload(String data, ReportFormattingStrategy strategy) {
        InputValidator.validateNotNull(data, "data");
        InputValidator.validateNotNull(strategy, "strategy");
        this.data = data;
        this.strategy = strategy;
    }
}

