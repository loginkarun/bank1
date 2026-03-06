package com.myproject.services.impl;

import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of ProductService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Product findById(UUID productId) {
        log.debug("Finding product with ID: {}", productId);
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID productId) {
        log.debug("Checking if product exists with ID: {}", productId);
        return productRepository.existsById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasStock(UUID productId, int quantity) {
        log.debug("Checking stock for product ID: {} with quantity: {}", productId, quantity);
        Product product = findById(productId);
        return product.hasStock(quantity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());
        return productRepository.save(product);
    }
}