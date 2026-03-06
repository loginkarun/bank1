package com.myproject.services.impl;

import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of ProductService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public Product getProductById(UUID productId) {
        log.debug("Fetching product with ID: {}", productId);
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    }
    
    @Override
    public void validateStock(UUID productId, int quantity) {
        log.debug("Validating stock for product: {}, quantity: {}", productId, quantity);
        Product product = getProductById(productId);
        
        if (product.getStock() < quantity) {
            throw new OutOfStockException(productId, quantity, product.getStock());
        }
    }
}
