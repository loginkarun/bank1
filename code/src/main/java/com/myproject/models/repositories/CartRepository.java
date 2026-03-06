package com.myproject.models.repositories;

import com.myproject.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Cart entity
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    /**
     * Find cart by user ID
     */
    Optional<Cart> findByUserId(UUID userId);
    
    /**
     * Find cart by session ID
     */
    Optional<Cart> findBySessionId(String sessionId);
    
    /**
     * Delete cart by user ID
     */
    void deleteByUserId(UUID userId);
    
    /**
     * Delete cart by session ID
     */
    void deleteBySessionId(String sessionId);
}
