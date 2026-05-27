package com.example.demo.repository;

import com.example.demo.dto.WorkGuestSummaryProjection;
import com.example.demo.dto.WorkListProjection;
import com.example.demo.entity.Work;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
  Optional<Work> findByWorkNumber(String workNumber);

  List<Work> findByOrderIdAndDeletedAtIsNull(Long orderId);

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

              r.id as resellerId,
              r.name as resellerName,

              pc.id as picContactId,
              pc.name as picName,

              os.isPrivate as isPrivate,

              s.id as serviceId,
              s.name as serviceName,

              a.id as areaId,
              a.name as areaName

          FROM Work w
          LEFT JOIN Order o ON o.id = w.orderId
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
          LEFT JOIN w.order o
          WHERE w.deletedAt IS NULL
      """)
  WorkGuestSummaryProjection getGuestSummary();
}
