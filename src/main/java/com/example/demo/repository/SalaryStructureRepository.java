package com.example.demo.repository;

import com.example.demo.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {
    List<SalaryStructure> findByGuideIdAndDeletedAtIsNull(Long guideId);
    Optional<SalaryStructure> findByGuideIdAndEffectiveFromLessThanEqualAndEffectiveUntilGreaterThanEqualAndDeletedAtIsNull(
        Long guideId, LocalDate date1, LocalDate date2);
    List<SalaryStructure> findByDeletedAtIsNull();
}

