package com.example.demo.repository;

import com.example.demo.dto.OrderSpecialRequestProjection;
import com.example.demo.dto.WorkDetailProjection;
import com.example.demo.dto.WorkGuestSummaryProjection;
import com.example.demo.dto.WorkGuideDetailProjection;
import com.example.demo.dto.WorkGuideProjection;
import com.example.demo.dto.WorkListProjection;
import com.example.demo.dto.WorkOrderListProjection;
import com.example.demo.dto.WorkOrderProjection;
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
              w.tourDate as tourDate
          FROM Work w
          WHERE w.deletedAt IS NULL
      """)
  Page<WorkListProjection> findWorkPage(Pageable pageable);

  @Query("""
          SELECT
              w.id as workId,

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
          JOIN w.orders o

          LEFT JOIN o.reseller r
          LEFT JOIN o.picContact pc

          LEFT JOIN OrderService os
              ON os.order.id = o.id
             AND os.deletedAt IS NULL

          LEFT JOIN os.service s
          LEFT JOIN s.area a

          WHERE w.id IN :workIds
            AND w.deletedAt IS NULL
      """)
  List<WorkOrderProjection> findOrdersByWorkIds(
      @Param("workIds") List<Long> workIds);

  @Query("""
          SELECT
              a.workId as workId,

              g.id as guideId,
              g.fullName as guideFullName,
              g.email as guideEmail,
              g.phone as guidePhone,
              g.isActive as guideIsActive,
              g.avatar as guideAvatar,

              a.role as guideRole

          FROM Assignment a
          JOIN a.guide g

          WHERE a.workId IN :workIds
            AND a.deletedAt IS NULL
            AND g.isActive = true
      """)
  List<WorkGuideProjection> findGuidesByWorkIds(
      @Param("workIds") List<Long> workIds);

  @Query("""
          SELECT
              COALESCE(SUM(o.adultCount), 0) as totalAdults,
              COALESCE(SUM(o.childCount), 0) as totalChildren
          FROM Work w
          LEFT JOIN w.orders o
          WHERE w.deletedAt IS NULL
      """)
  WorkGuestSummaryProjection getGuestSummary();

  @Query(value = """
          SELECT
              w.id,
              w.work_number as workNumber,
              w.status,
              w.tour_date as tourDate,
              w.tour_start_time as tourStartTime,
              w.tour_end_time as tourEndTime,
              w.location_name as locationName,
              w.location_address as locationAddress,
              w.notes,

              COALESCE(SUM(o.adult_count),0) as adultCount,
              COALESCE(SUM(o.child_count),0) as childCount,

              (
                  SELECT o2.is_private
                  FROM work_orders wo2
                  JOIN orders o2
                      ON o2.id = wo2.order_id
                  WHERE wo2.work_id = w.id
                  ORDER BY o2.id
                  LIMIT 1
              ) as isPrivate,

              (
                  SELECT s.id
                  FROM work_orders wo3
                  JOIN order_services os
                      ON os.order_id = wo3.order_id
                  JOIN services s
                      ON s.id = os.service_id
                  WHERE wo3.work_id = w.id
                    AND os.deleted_at IS NULL
                  ORDER BY os.id
                  LIMIT 1
              ) as serviceId,

              (
                  SELECT s.name
                  FROM work_orders wo3
                  JOIN order_services os
                      ON os.order_id = wo3.order_id
                  JOIN services s
                      ON s.id = os.service_id
                  WHERE wo3.work_id = w.id
                    AND os.deleted_at IS NULL
                  ORDER BY os.id
                  LIMIT 1
              ) as serviceName,

              (
                  SELECT a.id
                  FROM work_orders wo3
                  JOIN order_services os
                      ON os.order_id = wo3.order_id
                  JOIN services s
                      ON s.id = os.service_id
                  JOIN areas a
                      ON a.id = s.area_id
                  WHERE wo3.work_id = w.id
                    AND os.deleted_at IS NULL
                  ORDER BY os.id
                  LIMIT 1
              ) as areaId,

              (
                  SELECT a.name
                  FROM work_orders wo3
                  JOIN order_services os
                      ON os.order_id = wo3.order_id
                  JOIN services s
                      ON s.id = os.service_id
                  JOIN areas a
                      ON a.id = s.area_id
                  WHERE wo3.work_id = w.id
                    AND os.deleted_at IS NULL
                  ORDER BY os.id
                  LIMIT 1
              ) as areaName

          FROM works w
          LEFT JOIN work_orders wo
              ON wo.work_id = w.id
          LEFT JOIN orders o
              ON o.id = wo.order_id

          WHERE w.id = :id
            AND w.deleted_at IS NULL

          GROUP BY w.id
      """, nativeQuery = true)
  Optional<WorkDetailProjection> findWorkDetailById(
      @Param("id") Long id);

  @Query("""
          SELECT
              o.id as orderId,

              r.name as reseller,

              ag.name as originalAgent,

              o.ref1 as ref1,
              o.adultCount as adultCount,
              o.childCount as childCount,
              o.totalFeeAmount as totalFeeAmount,

              st.label as status

          FROM Work w
          JOIN w.orders o

          LEFT JOIN o.reseller r
          LEFT JOIN o.originalAgent ag
          LEFT JOIN o.status st

          WHERE w.id = :workId
          AND (
              :status = 'all'
              OR (
                  :status = 'active'
                  AND st.code NOT IN ('CANCELLED', 'ENDED', 'CLOSED')
              )
          )
      """)
  List<WorkOrderListProjection> findOrdersByWorkId(
      @Param("workId") Long workId,
      @Param("status") String status);

  @Query("""
          SELECT
              o.id as orderId,

              srt.id as specialRequestId,
              srt.code as specialRequestCode,
              srt.label as specialRequestLabel

          FROM Work w
          JOIN w.orders o

          JOIN OrderSpecialRequest osr
              ON osr.order.id = o.id

          JOIN osr.specialRequestType srt

          WHERE w.id = :workId
      """)
  List<OrderSpecialRequestProjection> findSpecialRequestsByWorkId(
      @Param("workId") Long workId);

  @Query("""
          SELECT
              g.fullName as name,
              g.phone as phone,

              a.id as id,
              a.guideId as guideId,
              a.status as status,
              a.note as note,
              a.isCalendarInvitation as isCalendarInvitation,
              a.role as role,
              a.rejectionReason as rejectionReason

          FROM Assignment a
          JOIN a.guide g

          WHERE a.workId = :workId
            AND a.deletedAt IS NULL
      """)
  List<WorkGuideDetailProjection> findGuidesByWorkId(
      @Param("workId") Long workId);
}
