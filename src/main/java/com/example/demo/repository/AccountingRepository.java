package com.example.demo.repository;

import com.example.demo.dto.AccountingFilter;
import com.example.demo.dto.AccountingGuideProjection;
import com.example.demo.dto.AccountingRowProjection;
import com.example.demo.entity.Work;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Dedicated repository for the Accounting page.
 * All queries are isolated from the existing WorkRepository.
 */
@Repository
public interface AccountingRepository extends JpaRepository<Work, Long> {

  // ── 1. Count query (for pagination total) ─────────────────────────────────

  @Query(value = """
      SELECT COUNT(DISTINCT w.id)
      FROM works w
      LEFT JOIN work_orders wo ON wo.work_id = w.id
      LEFT JOIN orders o       ON o.id = wo.order_id AND o.deleted_at IS NULL
      LEFT JOIN resellers r    ON r.id = o.reseller_id
      LEFT JOIN order_services os
             ON os.order_id = o.id AND os.deleted_at IS NULL
      LEFT JOIN services s     ON s.id = os.service_id AND s.deleted_at IS NULL
      LEFT JOIN assignments a
             ON a.work_id = w.id AND a.deleted_at IS NULL
      LEFT JOIN guides g       ON g.id = a.guide_id
      WHERE w.deleted_at IS NULL
        AND (:resellerId   IS NULL OR r.id = :resellerId)
        AND (:ref          IS NULL OR LOWER(o.ref1) LIKE LOWER(CONCAT('%', :ref, '%')))
        AND (:guideName    IS NULL OR LOWER(g.full_name) LIKE LOWER(CONCAT('%', :guideName, '%')))
        AND (:serviceName  IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :serviceName, '%')))
        AND (:tourDate     IS NULL OR w.tour_date = :tourDate)
        AND (:status       IS NULL OR UPPER(w.status) = UPPER(:status))
        AND (:isPrivate    IS NULL OR o.is_private = :isPrivate)
      """, nativeQuery = true)
  long countAccounting(
      @Param("resellerId")  Long resellerId,
      @Param("ref")         String ref,
      @Param("guideName")   String guideName,
      @Param("serviceName") String serviceName,
      @Param("tourDate")    LocalDate tourDate,
      @Param("status")      String status,
      @Param("isPrivate")   Boolean isPrivate);

  // ── 2. Page of work IDs (filtered, sorted, paginated) ─────────────────────

  @Query(value = """
      SELECT DISTINCT w.id
      FROM works w
      LEFT JOIN work_orders wo ON wo.work_id = w.id
      LEFT JOIN orders o       ON o.id = wo.order_id AND o.deleted_at IS NULL
      LEFT JOIN resellers r    ON r.id = o.reseller_id
      LEFT JOIN order_services os
             ON os.order_id = o.id AND os.deleted_at IS NULL
      LEFT JOIN services s     ON s.id = os.service_id AND s.deleted_at IS NULL
      LEFT JOIN assignments a
             ON a.work_id = w.id AND a.deleted_at IS NULL
      LEFT JOIN guides g       ON g.id = a.guide_id
      WHERE w.deleted_at IS NULL
        AND (:resellerId   IS NULL OR r.id = :resellerId)
        AND (:ref          IS NULL OR LOWER(o.ref1) LIKE LOWER(CONCAT('%', :ref, '%')))
        AND (:guideName    IS NULL OR LOWER(g.full_name) LIKE LOWER(CONCAT('%', :guideName, '%')))
        AND (:serviceName  IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :serviceName, '%')))
        AND (:tourDate     IS NULL OR w.tour_date = :tourDate)
        AND (:status       IS NULL OR UPPER(w.status) = UPPER(:status))
        AND (:isPrivate    IS NULL OR o.is_private = :isPrivate)
      ORDER BY w.id DESC
      LIMIT :pageSize OFFSET :offset
      """, nativeQuery = true)
  List<Long> findAccountingWorkIds(
      @Param("resellerId")  Long resellerId,
      @Param("ref")         String ref,
      @Param("guideName")   String guideName,
      @Param("serviceName") String serviceName,
      @Param("tourDate")    LocalDate tourDate,
      @Param("status")      String status,
      @Param("isPrivate")   Boolean isPrivate,
      @Param("pageSize")    int pageSize,
      @Param("offset")      long offset);

  // ── 3. Main data query — one row per work ─────────────────────────────────

  @Query(value = """
      SELECT
          w.id                AS workId,
          w.work_number       AS workNumber,
          w.status            AS workStatus,
          w.tour_date         AS tourDate,
          w.tour_start_time   AS tourStartTime,
          w.notes             AS notes,

          (SELECT o2.ref1
           FROM work_orders wo2
           JOIN orders o2 ON o2.id = wo2.order_id AND o2.deleted_at IS NULL
           WHERE wo2.work_id = w.id
           ORDER BY o2.id
           LIMIT 1)           AS ref1,

          (SELECT r2.name
           FROM work_orders wo2
           JOIN orders o2 ON o2.id = wo2.order_id AND o2.deleted_at IS NULL
           JOIN resellers r2 ON r2.id = o2.reseller_id
           WHERE wo2.work_id = w.id
           ORDER BY o2.id
           LIMIT 1)           AS resellerName,

          (SELECT o2.is_private
           FROM work_orders wo2
           JOIN orders o2 ON o2.id = wo2.order_id AND o2.deleted_at IS NULL
           WHERE wo2.work_id = w.id
           ORDER BY o2.id
           LIMIT 1)           AS isPrivate,

          (SELECT COALESCE(SUM(o2.adult_count), 0)
           FROM work_orders wo2
           JOIN orders o2 ON o2.id = wo2.order_id AND o2.deleted_at IS NULL
           WHERE wo2.work_id = w.id)  AS adultCount,

          (SELECT COALESCE(SUM(o2.child_count), 0)
           FROM work_orders wo2
           JOIN orders o2 ON o2.id = wo2.order_id AND o2.deleted_at IS NULL
           WHERE wo2.work_id = w.id)  AS childCount,

          (SELECT s2.name
           FROM work_orders wo2
           JOIN orders o2     ON o2.id = wo2.order_id AND o2.deleted_at IS NULL
           JOIN order_services os2 ON os2.order_id = o2.id AND os2.deleted_at IS NULL
           JOIN services s2   ON s2.id = os2.service_id AND s2.deleted_at IS NULL
           WHERE wo2.work_id = w.id
           ORDER BY o2.id, os2.id
           LIMIT 1)           AS serviceName

      FROM works w
      WHERE w.id IN (:workIds)
        AND w.deleted_at IS NULL
      ORDER BY w.id DESC
      """, nativeQuery = true)
  List<AccountingRowProjection> findAccountingRows(@Param("workIds") List<Long> workIds);

  // ── 4. All guide names per work ────────────────────────────────────────────

  @Query(value = """
      SELECT
          a.work_id   AS workId,
          g.full_name AS guideName
      FROM assignments a
      JOIN guides g ON g.id = a.guide_id
      WHERE a.work_id IN (:workIds)
        AND a.deleted_at IS NULL
      ORDER BY a.work_id, a.id
      """, nativeQuery = true)
  List<AccountingGuideProjection> findAccountingGuides(@Param("workIds") List<Long> workIds);
}
