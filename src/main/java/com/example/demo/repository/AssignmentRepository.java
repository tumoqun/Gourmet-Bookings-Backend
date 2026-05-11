package com.example.demo.repository;

import com.example.demo.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByGuideIdOrderByAssignedAtDesc(@Param("guideId") Long guideId);
    
    List<Assignment> findByOrderServiceId(@Param("orderServiceId") Long orderServiceId);
    
    List<Assignment> findByStatus(@Param("status") String status);
    
    @Query("SELECT a FROM Assignment a WHERE a.guide.id = :guideId AND a.assignedAt >= :startDate ORDER BY a.assignedAt DESC")
    List<Assignment> findByGuideIdAndAssignedAtAfter(@Param("guideId") Long guideId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT a FROM Assignment a WHERE a.orderService.order.deletedAt IS NULL ORDER BY a.assignedAt DESC")
    List<Assignment> findAllActive();
}
