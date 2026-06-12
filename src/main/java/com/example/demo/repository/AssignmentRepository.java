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
    
    List<Assignment> findByGuideIdOrderByCreatedAtDesc(@Param("guideId") Long guideId);
    
    List<Assignment> findByWorkId(@Param("workId") Long workId);
    
    List<Assignment> findByWorkIdIn(List<Long> workIds);

    List<Assignment> findByStatus(@Param("status") String status);
    
    @Query("SELECT a FROM Assignment a WHERE a.guideId = :guideId AND a.createdAt >= :startDate ORDER BY a.createdAt DESC")
    List<Assignment> findByGuideIdAndCreatedAtAfter(@Param("guideId") Long guideId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT a FROM Assignment a WHERE a.deletedAt IS NULL ORDER BY a.createdAt DESC")
    List<Assignment> findAllActive();

    boolean existsByWorkIdAndGuideIdAndDeletedAtIsNull(Long workId, Long guideId);
}
