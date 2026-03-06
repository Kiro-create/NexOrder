package com.eoms.config;

public class SystemConfig {

    private static SystemConfig instance;

    private String paymentGatewayUrl;
    private String courierApiUrl;
    private int reorderThreshold;
    private double taxRate;
    private double shippingFlatRate;

    private SystemConfig() {
        paymentGatewayUrl = "https://api.paymentgateway.com";
        courierApiUrl = "https://api.courier.com";
        reorderThreshold = 5;
        taxRate = 0.14;
        shippingFlatRate = 50.0;
    }

    public static synchronized SystemConfig getInstance() {
        if (instance == null) {
            instance = new SystemConfig();
        }
        return instance;
    }

    public String getPaymentGatewayUrl() {
        return paymentGatewayUrl;
    }

    public String getCourierApiUrl() {
        return courierApiUrl;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getShippingFlatRate() {
        return shippingFlatRate;
    }
}