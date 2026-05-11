package com.example.demo.repository;

import com.example.demo.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    
    Optional<OrderStatus> findByCode(String code);
    
    Optional<OrderStatus> findByLabel(String label);
}
