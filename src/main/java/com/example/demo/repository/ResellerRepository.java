package com.example.demo.repository;

import com.example.demo.entity.Reseller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResellerRepository extends JpaRepository<Reseller, Long> {
    
    Optional<Reseller> findByName(String name);
    
    List<Reseller> findByStatus(@Param("status") String status);
    
    @Query("SELECT r FROM Reseller r WHERE r.status = 'ACTIVE' ORDER BY r.name")
    List<Reseller> findActiveResellers();
    
    @Query("SELECT r FROM Reseller r ORDER BY r.name")
    List<Reseller> findAllOrdered();
}
