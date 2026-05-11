package com.example.demo.repository;

import com.example.demo.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    List<Service> findByAreaIdAndServiceTypeIdAndIsActiveTrue(@Param("areaId") Long areaId, @Param("serviceTypeId") Long serviceTypeId);
    
    List<Service> findByAreaIdAndIsActiveTrue(@Param("areaId") Long areaId);
    
    List<Service> findByServiceTypeIdAndIsActiveTrue(@Param("serviceTypeId") Long serviceTypeId);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true AND s.isPrivateAvailable = true")
    List<Service> findPrivateServices();
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true ORDER BY s.area.name, s.serviceType.name, s.name")
    List<Service> findAllActive();
}
