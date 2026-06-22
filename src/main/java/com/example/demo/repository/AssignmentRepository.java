package com.example.demo.repository;

import com.example.demo.dto.AssignmentListProjection;
import com.example.demo.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByGuideIdOrderByCreatedAtDesc(@Param("guideId") Long guideId);

    List<Assignment> findByWorkId(@Param("workId") Long workId);

    List<Assignment> findByWorkIdIn(List<Long> workIds);

    List<Assignment> findByStatus(@Param("status") String status);

    @Query("SELECT a FROM Assignment a WHERE a.guideId = :guideId AND a.createdAt >= :startDate ORDER BY a.createdAt DESC")
    List<Assignment> findByGuideIdAndCreatedAtAfter(@Param("guideId") Long guideId,
            @Param("startDate") LocalDateTime startDate);

    @Query("SELECT a FROM Assignment a WHERE a.deletedAt IS NULL ORDER BY a.createdAt DESC")
    List<Assignment> findAllActive();

    boolean existsByWorkIdAndGuideIdAndDeletedAtIsNull(Long workId, Long guideId);

    @Query(value = """
            SELECT
                a.id AS assignmentId,
                a.status AS assignmentStatus,

                w.id AS workId,
                w.status AS workStatus,
                w.tour_date AS tourDate,
                w.tour_start_time AS tourStartTime,
                w.tour_end_time AS tourEndTime,

                CASE
                    WHEN w.tour_start_time IS NOT NULL
                     AND w.tour_end_time IS NOT NULL
                    THEN EXTRACT(
                            EPOCH FROM (
                                w.tour_end_time - w.tour_start_time
                            )
                         ) / 60
                    ELSE 0
                END AS durationMinutes,

                MAX(os.service_name_snapshot) AS serviceName,

                COALESCE(SUM(o.adult_count), 0) AS totalAdultCount,
                COALESCE(SUM(o.child_count), 0) AS totalChildCount,

                COALESCE(SUM(o.total_fee_amount), 0) AS totalFeeAmount,

                STRING_AGG(DISTINCT r.name, ', ') AS resellerName,

                COUNT(DISTINCT o.id) AS orderCount

            FROM assignments a

            INNER JOIN works w
                ON w.id = a.work_id
               AND w.deleted_at IS NULL

            INNER JOIN work_orders wo
                ON wo.work_id = w.id

            INNER JOIN orders o
                ON o.id = wo.order_id
               AND o.deleted_at IS NULL

            LEFT JOIN order_services os
                ON os.order_id = o.id
               AND os.deleted_at IS NULL

            LEFT JOIN resellers r
                ON r.id = o.reseller_id

            WHERE a.deleted_at IS NULL

            AND (:guideId IS NULL OR a.guide_id = :guideId)
            AND COALESCE(CAST(:requestedDate AS DATE), w.tour_date) = w.tour_date
            AND COALESCE(:status, 'IN_PREP') <> w.status
            AND COALESCE(:status, w.status) = w.status

            GROUP BY
                a.id,
                a.status,
                w.id,
                w.status,
                w.tour_date,
                w.tour_start_time,
                w.tour_end_time

            ORDER BY
                w.tour_date DESC,
                w.tour_start_time ASC,
                a.created_at DESC
            """, nativeQuery = true)
    List<AssignmentListProjection> findAssignments(
            @Param("guideId") Long guideId,
            @Param("requestedDate") LocalDate requestedDate,
            @Param("status") String status);

    @Modifying
    @Query("""
                UPDATE Assignment a
                SET a.role = 'guide',
                    a.updatedAt = :updatedAt
                WHERE a.workId = :workId
                  AND a.deletedAt IS NULL
            """)
    void updateAllRolesToGuide(
            @Param("workId") Long workId,
            @Param("updatedAt") LocalDateTime updatedAt);
}
