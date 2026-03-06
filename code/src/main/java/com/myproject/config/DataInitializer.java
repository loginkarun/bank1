package com.myproject.config;

import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Initialize sample data for testing
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        log.info("Initializing sample products...");
        
        List<Product> products = Arrays.asList(
            createProduct("Wireless Mouse", new BigDecimal("29.99"), 100),
            createProduct("Mechanical Keyboard", new BigDecimal("89.99"), 50),
            createProduct("USB-C Hub", new BigDecimal("49.99"), 75),
            createProduct("Laptop Stand", new BigDecimal("39.99"), 30),
            createProduct("Webcam HD", new BigDecimal("79.99"), 20)
        );
        
        productRepository.saveAll(products);
        log.info("Sample products initialized: {} products created", products.size());
    }

    private Product createProduct(String name, BigDecimal price, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        return product;
    }
}