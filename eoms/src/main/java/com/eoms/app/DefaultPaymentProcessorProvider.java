package com.eoms.app;

import com.eoms.adapter.PaymentGatewayAdapter;
import com.eoms.config.Logger;
import com.eoms.factory.CashOnDeliveryPayment;
import com.eoms.factory.CreditCardProcessor;
import com.eoms.factory.PaymentMethod;
import com.eoms.factory.PaymentProcessor;
import com.eoms.factory.PayPalProcessor;
import com.eoms.factory.decorator.MaxAmountPaymentProcessorDecorator;
import com.eoms.strategy.PaymentContext;
import com.eoms.strategy.PaymentMethodAdapter;

/**
 * Default wiring for the demo payment processors.
 */
public class DefaultPaymentProcessorProvider implements PaymentProcessorProvider {

    private static final double COD_CAP = 2000;

    @Override
    public PaymentProcessor getProcessor(int paymentChoice) {
        switch (paymentChoice) {
            case 1:
                return new PaymentGatewayAdapter(new CreditCardProcessor());
            case 2:
                return new PaymentGatewayAdapter(new PayPalProcessor());
            case 3:
                return new PaymentProcessor() {
                    @Override
                    public PaymentMethod createPayment() {
                        return new MaxAmountPaymentProcessorDecorator(new CashOnDeliveryPayment(), COD_CAP);
                    }

                    @Override
                    public String getPaymentStatus() {
                        return "Cash On Delivery";
                    }
                };
            default:
                Logger.getInstance().error("Invalid payment choice: " + paymentChoice);
                return null;
        }
    }

    @Override
    public PaymentContext getPaymentContext(int paymentChoice) {
        PaymentProcessor processor = getProcessor(paymentChoice);
        if (processor == null) {
            return null;
        }
        PaymentContext context = new PaymentContext();
        context.setStrategy(new PaymentMethodAdapter(processor));
        return context;
    }
}

