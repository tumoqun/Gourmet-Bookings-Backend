package com.example.demo.repository;

import com.example.demo.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByAssignmentIdAndDeletedAtIsNull(Long assignmentId);
    List<Receipt> findByAssignmentIdAndIsVerifiedAndDeletedAtIsNull(Long assignmentId, Boolean isVerified);
    List<Receipt> findBySupplierId(Long supplierId);
    List<Receipt> findByReceiptDateBetweenAndDeletedAtIsNull(LocalDate startDate, LocalDate endDate);
    List<Receipt> findByDeletedAtIsNull();
}

