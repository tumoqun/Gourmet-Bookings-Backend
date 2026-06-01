package com.example.demo.repository;

import com.example.demo.dto.WorkDetailProjection;
import com.example.demo.dto.WorkGuestSummaryProjection;
import com.example.demo.dto.WorkListProjection;
import com.example.demo.entity.Work;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
  Optional<Work> findByWorkNumber(String workNumber);

  List<Work> findByOrdersIdAndDeletedAtIsNull(Long orderId);

  List<Work> findByStatusAndDeletedAtIsNull(String status);

  List<Work> findByTourDateAndDeletedAtIsNull(LocalDate tourDate);

  @Query("""
          SELECT
              w.id as id,
              w.workNumber as workNumber,
              w.status as status,
              w.tourDate as tourDate,

              o.id as orderId,
              o.orderNumber as orderNumber,
              o.adultCount as adultCount,
              o.childCount as childCount,
              o.ref1 as ref1,

              r.id as resellerId,
              r.name as resellerName,

              pc.id as picContactId,
              pc.name as picName,

              o.isPrivate as isPrivate,

              s.id as serviceId,
              s.name as serviceName,

              a.id as areaId,
              a.name as areaName

          FROM Work w
          LEFT JOIN w.orders o
          LEFT JOIN o.reseller r
          LEFT JOIN o.picContact pc

          LEFT JOIN OrderService os ON os.order.id = o.id
              AND os.deletedAt IS NULL

          LEFT JOIN os.service s
          LEFT JOIN s.area a

          WHERE w.deletedAt IS NULL
      """)
  Page<WorkListProjection> findAllList(Pageable pageable);

  @Query("""
          SELECT
              COALESCE(SUM(o.adultCount), 0) as totalAdults,
              COALESCE(SUM(o.childCount), 0) as totalChildren
          FROM Work w
          LEFT JOIN w.orders o
          WHERE w.deletedAt IS NULL
      """)
  WorkGuestSummaryProjection getGuestSummary();

  @Query("""
          SELECT
              w.id as id,
              w.workNumber as workNumber,
              w.status as status,
              w.tourDate as tourDate,
              w.tourStartTime as tourStartTime,
              w.tourEndTime as tourEndTime,
              w.locationName as locationName,
              w.locationAddress as locationAddress,
              w.notes as notes,

              o.id as orderId,
              o.adultCount as adultCount,
              o.childCount as childCount,

              os.isPrivate as isPrivate,

              s.name as serviceName,

              a.id as areaId,
              a.name as areaName

          FROM Work w
          LEFT JOIN Order o ON o.id = w.orderId

          LEFT JOIN OrderService os ON os.id = (
              SELECT MIN(os2.id)
              FROM OrderService os2
              WHERE os2.order.id = o.id
              AND os2.deletedAt IS NULL
          )

          LEFT JOIN os.service s
          LEFT JOIN s.area a

          WHERE w.deletedAt IS NULL
          AND w.id = :id
      """)
  Optional<WorkDetailProjection> findWorkDetailById(@Param("id") Long id);
}
