package com.myproject.config;

import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Initialize sample data for testing
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing sample products...");
        
        if (productRepository.count() == 0) {
            Product product1 = Product.builder()
                .name("Wireless Mouse")
                .price(new BigDecimal("29.99"))
                .stock(100)
                .build();
            
            Product product2 = Product.builder()
                .name("Mechanical Keyboard")
                .price(new BigDecimal("89.99"))
                .stock(50)
                .build();
            
            Product product3 = Product.builder()
                .name("USB-C Cable")
                .price(new BigDecimal("12.99"))
                .stock(200)
                .build();
            
            Product product4 = Product.builder()
                .name("Laptop Stand")
                .price(new BigDecimal("45.00"))
                .stock(75)
                .build();
            
            Product product5 = Product.builder()
                .name("Webcam HD")
                .price(new BigDecimal("65.50"))
                .stock(30)
                .build();
            
            productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));
            log.info("Sample products initialized successfully");
        } else {
            log.info("Products already exist, skipping initialization");
        }
    }
}
