package com.example.demo.repository;

import com.example.demo.dto.WorkReceiptResponse;
import com.example.demo.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
                SELECT new com.example.demo.dto.WorkReceiptResponse(
                    r.id,
                    r.assignmentId,
                    r.supplierId,
                    s.name,
                    r.itineraryStopId,
                    r.receiptType,
                    r.description,
                    r.amount,
                    r.fee,
                    r.tax,
                    r.currencyCode,
                    r.receiptDate,
                    r.receiptTime,
                    r.receiptNumber,
                    r.category,
                    r.paymentMethod,
                    r.notes,
                    r.imageUrl,
                    r.isVerified,
                    r.submittedBy,
                    r.createdAt
                )
                FROM Receipt r
                JOIN Assignment a
                    ON a.id = r.assignmentId
                LEFT JOIN Supplier s
                    ON s.id = r.supplierId
                WHERE a.workId = :workId
                  AND r.deletedAt IS NULL
                  AND a.deletedAt IS NULL
                  AND (s IS NULL OR s.deletedAt IS NULL)
                ORDER BY r.receiptDate DESC,
                         r.receiptTime DESC,
                         r.id DESC
            """)
    List<WorkReceiptResponse> findReceiptsByWorkId(
            @Param("workId") Long workId);
}
