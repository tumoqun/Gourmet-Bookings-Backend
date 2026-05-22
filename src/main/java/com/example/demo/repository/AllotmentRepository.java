package com.example.demo.repository;

import com.example.demo.entity.Allotment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AllotmentRepository extends JpaRepository<Allotment, Long> {
    
    List<Allotment> findByServiceIdAndServiceDateAndStartTimeOrderByStartTime(
            @Param("serviceId") Long serviceId, 
            @Param("serviceDate") LocalDate serviceDate, 
            @Param("startTime") LocalTime startTime);
    
    List<Allotment> findByServiceIdAndServiceDateOrderByStartTime(
            @Param("serviceId") Long serviceId, 
            @Param("serviceDate") LocalDate serviceDate);
    
    @Query("SELECT a FROM Allotment a WHERE a.service.id = :serviceId AND a.serviceDate = :serviceDate ORDER BY a.startTime")
    List<Allotment> findByServiceAndDate(@Param("serviceId") Long serviceId, @Param("serviceDate") LocalDate serviceDate);

    @Query("SELECT a FROM Allotment a WHERE a.serviceDate = :serviceDate ORDER BY a.service.id, a.startTime")
    List<Allotment> findByDate(@Param("serviceDate") LocalDate serviceDate);
    
    @Query("SELECT a FROM Allotment a WHERE a.reservedTotal < a.capacityTotal AND a.status = 'ACTIVE'")
    List<Allotment> findAvailableAllotments();
}
