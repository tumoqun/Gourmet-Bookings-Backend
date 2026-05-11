package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByStatusIdAndRequestedAtGreaterThanOrderByRequestedAtDesc(Long statusId, LocalDateTime date);
    
    List<Order> findByResellerIdAndRequestedAtGreaterThanOrderByRequestedAtDesc(Long resellerId, LocalDateTime date);
    
    List<Order> findByPicContactIdOrderByRequestedAtDesc(Long picContactId);
    
    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL ORDER BY o.createdAt DESC")
    List<Order> findAllActive();
    
    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.status.id = :statusId ORDER BY o.createdAt DESC")
    List<Order> findByStatusId(@Param("statusId") Long statusId);
}
