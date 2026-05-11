package com.example.demo.repository;

import com.example.demo.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    
    Optional<Area> findByCode(String code);
    
    List<Area> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT a FROM Area a ORDER BY a.name")
    List<Area> findAllOrdered();
}
