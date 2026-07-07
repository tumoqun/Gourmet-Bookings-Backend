package com.example.demo.repository;

import com.example.demo.entity.SalaryScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryScaleRepository extends JpaRepository<SalaryScale, String> {
}
