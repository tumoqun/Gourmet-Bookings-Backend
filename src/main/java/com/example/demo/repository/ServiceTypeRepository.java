package com.example.demo.repository;

import com.example.demo.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    
    Optional<ServiceType> findByCode(String code);
    
    List<ServiceType> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT st FROM ServiceType st ORDER BY st.name")
    List<ServiceType> findAllOrdered();
}
