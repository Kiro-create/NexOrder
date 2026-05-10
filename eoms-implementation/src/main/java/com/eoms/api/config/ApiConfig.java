package com.eoms.api.config;

import com.eoms.DAO.*;
import com.eoms.entity.Product;
import com.eoms.service.*;
import com.eoms.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    public OrderInterface orderDAO() {
        return new OrderDAO();
    }

    @Bean
    public PaymentInterface paymentDAO() {
        return new PaymentDAO();
    }

    @Bean
    public ProductInterface productDAO() {
        ProductDAO productDAO = new ProductDAO();

        // Internal test products only.
        // We are not exposing Product APIs in Part 2.
        productDAO.saveProduct(Product.createProduct(
            10,
            "Laptop",
            15000.0,
            5,
            Product.ProductType.PHYSICAL
        ));

        productDAO.saveProduct(Product.createProduct(
            11,
            "Online Course",
            800.0,
            100,
            Product.ProductType.DIGITAL
        ));

        return productDAO;
    }

    @Bean
    public OrderService orderService(OrderInterface orderDAO, ProductInterface productDAO) {
        return new OrderServiceImpl(orderDAO, productDAO);
    }

    @Bean
    public PaymentService paymentService(PaymentInterface paymentDAO) {
        return new PaymentServiceImpl(paymentDAO);
    }
}