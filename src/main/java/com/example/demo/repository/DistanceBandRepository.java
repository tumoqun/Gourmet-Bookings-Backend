package com.example.demo.repository;

import com.example.demo.entity.DistanceBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DistanceBandRepository extends JpaRepository<DistanceBand, Long> {
    @Query("SELECT db FROM DistanceBand db ORDER BY db.sortOrder")
    List<DistanceBand> findAllOrdered();
}
