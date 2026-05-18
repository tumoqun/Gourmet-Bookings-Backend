package com.example.demo.repository;

import com.example.demo.entity.SalaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryEntryRepository extends JpaRepository<SalaryEntry, Long> {
    List<SalaryEntry> findByAssignmentId(Long assignmentId);
    List<SalaryEntry> findByGuideId(Long guideId);
    List<SalaryEntry> findByGuideIdAndPaymentStatusOrderByPeriodEndDateDesc(Long guideId, String paymentStatus);
    List<SalaryEntry> findByWorkId(Long workId);
    List<SalaryEntry> findByPeriodStartDateBetweenAndPaymentStatus(LocalDate startDate, LocalDate endDate, String paymentStatus);
}

