package com.example.demo.repository;

import com.example.demo.entity.OrderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderService, Long> {
    
    List<OrderService> findByTargetDateAndAreaIdAndServiceIdOrderByStartTime(
            @Param("targetDate") LocalDate targetDate, 
            @Param("areaId") Long areaId, 
            @Param("serviceId") Long serviceId);
    
    List<OrderService> findByOrderId(Long orderId);
    
    List<OrderService> findByServiceIdAndTargetDate(Long serviceId, LocalDate targetDate);
    
    @Query("SELECT os FROM OrderService os WHERE os.order.deletedAt IS NULL ORDER BY os.targetDate, os.startTime")
    List<OrderService> findAllActive();
}
