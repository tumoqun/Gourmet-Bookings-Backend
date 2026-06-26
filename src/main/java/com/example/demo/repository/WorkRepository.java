package com.example.demo.repository;

import com.example.demo.dto.GuestProjection;
import com.example.demo.dto.OrderSpecialRequestProjection;
import com.example.demo.dto.WorkDetailForGuideProjection;
import com.example.demo.dto.WorkDetailProjection;
import com.example.demo.dto.WorkFilter;
import com.example.demo.dto.WorkGuideDetailProjection;
import com.example.demo.dto.WorkGuideProjection;
import com.example.demo.dto.WorkListProjection;
import com.example.demo.dto.WorkOrderForGuideProjection;
import com.example.demo.dto.WorkOrderGuestProjection;
import com.example.demo.dto.WorkOrderListProjection;
import com.example.demo.dto.WorkOrderProjection;
import com.example.demo.entity.Work;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
              SELECT DISTINCT
                  w.id as id,
                  w.workNumber as workNumber,
                  w.status as status,
                  w.tourDate as tourDate
              FROM Work w
              LEFT JOIN w.orders o
              LEFT JOIN o.reseller r
              LEFT JOIN o.picContact pc
              LEFT JOIN OrderService os
                  ON os.order.id = o.id
                 AND os.deletedAt IS NULL
              LEFT JOIN os.service s
              LEFT JOIN s.area a
              LEFT JOIN Assignment ass
                  ON ass.workId = w.id
                 AND ass.deletedAt IS NULL
              LEFT JOIN ass.guide g
              WHERE w.deletedAt IS NULL
                AND (
                      :#{#filter.resellerId} IS NULL
                      OR r.id = :#{#filter.resellerId}
                )
                AND (
                      :#{#filter.ref} IS NULL
                      OR LOWER(o.ref1)
                          LIKE LOWER(CONCAT('%', :#{#filter.ref}, '%'))
                )
                AND (
                      :#{#filter.personInChargeId} IS NULL
                      OR pc.id = :#{#filter.personInChargeId}
                )
                AND (
                      :#{#filter.areaId} IS NULL
                      OR a.id = :#{#filter.areaId}
                )
                AND (
                      :#{#filter.serviceName} IS NULL
                      OR LOWER(s.name)
                          LIKE LOWER(CONCAT('%', :#{#filter.serviceName}, '%'))
                )
                AND (
                      :#{#filter.guideName} IS NULL
                      OR LOWER(g.fullName)
                          LIKE LOWER(CONCAT('%', :#{#filter.guideName}, '%'))
                )
                AND (
                      :#{#filter.status} IS NULL
                      OR w.status = :#{#filter.status}
                )
                AND (
                      :#{#filter.privateFilter} IS NULL
                      OR o.isPrivate = :#{#filter.privateFilter}
                )
          """)
  Page<WorkListProjection> findWorkPage(
          @Param("filter") WorkFilter filter,
          Pageable pageable);

    @Query("""
              SELECT DISTINCT
                  w.id as id,
                  w.workNumber as workNumber,
                  w.status as status,
                  w.tourDate as tourDate
              FROM Work w
              LEFT JOIN w.orders o
              LEFT JOIN o.reseller r
              LEFT JOIN o.picContact pc
              LEFT JOIN OrderService os
                  ON os.order.id = o.id
                 AND os.deletedAt IS NULL
              LEFT JOIN os.service s
              LEFT JOIN s.area a
              LEFT JOIN Assignment ass
                  ON ass.workId = w.id
                 AND ass.deletedAt IS NULL
              LEFT JOIN ass.guide g
              WHERE w.deletedAt IS NULL
                AND w.tourDate = :tourDate
                AND (
                      :#{#filter.resellerId} IS NULL
                      OR r.id = :#{#filter.resellerId}
                )
                AND (
                      :#{#filter.ref} IS NULL
                      OR LOWER(o.ref1)
                          LIKE LOWER(CONCAT('%', :#{#filter.ref}, '%'))
                )
                AND (
                      :#{#filter.personInChargeId} IS NULL
                      OR pc.id = :#{#filter.personInChargeId}
                )
                AND (
                      :#{#filter.areaId} IS NULL
                      OR a.id = :#{#filter.areaId}
                )
                AND (
                      :#{#filter.serviceName} IS NULL
                      OR LOWER(s.name)
                          LIKE LOWER(CONCAT('%', :#{#filter.serviceName}, '%'))
                )
                AND (
                      :#{#filter.guideName} IS NULL
                      OR LOWER(g.fullName)
                          LIKE LOWER(CONCAT('%', :#{#filter.guideName}, '%'))
                )
                AND (
                      :#{#filter.status} IS NULL
                      OR w.status = :#{#filter.status}
                )
                AND (
                      :#{#filter.privateFilter} IS NULL
                      OR o.isPrivate = :#{#filter.privateFilter}
                )
          """)
  Page<WorkListProjection> findWorkPageByTourDate(
          @Param("filter") WorkFilter filter,
          @Param("tourDate") LocalDate tourDate,
          Pageable pageable);

  @Query("""
              SELECT DISTINCT w.id
              FROM Work w
              LEFT JOIN w.orders o
              LEFT JOIN o.reseller r
              LEFT JOIN o.picContact pc
              LEFT JOIN OrderService os
                  ON os.order.id = o.id
                 AND os.deletedAt IS NULL
              LEFT JOIN os.service s
              LEFT JOIN s.area a
              LEFT JOIN Assignment ass
                  ON ass.workId = w.id
                 AND ass.deletedAt IS NULL
              LEFT JOIN ass.guide g
              WHERE w.deletedAt IS NULL
                AND (
                      :#{#filter.resellerId} IS NULL
                      OR r.id = :#{#filter.resellerId}
                )
                AND (
                      :#{#filter.ref} IS NULL
                      OR LOWER(o.ref1)
                          LIKE LOWER(CONCAT('%', :#{#filter.ref}, '%'))
                )
                AND (
                      :#{#filter.personInChargeId} IS NULL
                      OR pc.id = :#{#filter.personInChargeId}
                )
                AND (
                      :#{#filter.areaId} IS NULL
                      OR a.id = :#{#filter.areaId}
                )
                AND (
                      :#{#filter.serviceName} IS NULL
                      OR LOWER(s.name)
                          LIKE LOWER(CONCAT('%', :#{#filter.serviceName}, '%'))
                )
                AND (
                      :#{#filter.guideName} IS NULL
                      OR LOWER(g.fullName)
                          LIKE LOWER(CONCAT('%', :#{#filter.guideName}, '%'))
                )
                AND (
                      :#{#filter.status} IS NULL
                      OR w.status = :#{#filter.status}
                )
                AND (
                      :#{#filter.privateFilter} IS NULL
                      OR o.isPrivate = :#{#filter.privateFilter}
                )
          """)
  List<Long> findAllWorkIds(
          @Param("filter") WorkFilter filter);

  @Query("""
              SELECT DISTINCT w.id
              FROM Work w
              LEFT JOIN w.orders o
              LEFT JOIN o.reseller r
              LEFT JOIN o.picContact pc
              LEFT JOIN OrderService os
                  ON os.order.id = o.id
                 AND os.deletedAt IS NULL
              LEFT JOIN os.service s
              LEFT JOIN s.area a
              LEFT JOIN Assignment ass
                  ON ass.workId = w.id
                 AND ass.deletedAt IS NULL
              LEFT JOIN ass.guide g
              WHERE w.deletedAt IS NULL
                AND w.tourDate = :tourDate
                AND (
                      :#{#filter.resellerId} IS NULL
                      OR r.id = :#{#filter.resellerId}
                )
                AND (
                      :#{#filter.ref} IS NULL
                      OR LOWER(o.ref1)
                          LIKE LOWER(CONCAT('%', :#{#filter.ref}, '%'))
                )
                AND (
                      :#{#filter.personInChargeId} IS NULL
                      OR pc.id = :#{#filter.personInChargeId}
                )
                AND (
                      :#{#filter.areaId} IS NULL
                      OR a.id = :#{#filter.areaId}
                )
                AND (
                      :#{#filter.serviceName} IS NULL
                      OR LOWER(s.name)
                          LIKE LOWER(CONCAT('%', :#{#filter.serviceName}, '%'))
                )
                AND (
                      :#{#filter.guideName} IS NULL
                      OR LOWER(g.fullName)
                          LIKE LOWER(CONCAT('%', :#{#filter.guideName}, '%'))
                )
                AND (
                      :#{#filter.status} IS NULL
                      OR w.status = :#{#filter.status}
                )
                AND (
                      :#{#filter.privateFilter} IS NULL
                      OR o.isPrivate = :#{#filter.privateFilter}
                )
          """)
  List<Long> findAllWorkIdsByTourDate(
          @Param("filter") WorkFilter filter,
          @Param("tourDate") LocalDate tourDate);

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

    @Query(value = """
          SELECT
              w.id AS workId,
              w.status AS status,
              w.tour_date AS tourDate,
              w.tour_start_time AS tourStartTime,
              w.tour_end_time AS tourEndTime,
              CAST(
                EXTRACT(EPOCH FROM (w.tour_end_time - w.tour_start_time))/60
                AS BIGINT
            ) AS durationMinutes,
              w.location_address AS locationAddress,
              a.name AS agentName,
              r.name AS resellerName,
              o.ref1 AS ref1,
              o.ref2 AS ref2,
              s.name AS serviceName
          FROM works w
          JOIN work_orders wo
              ON wo.work_id = w.id
          JOIN orders o
              ON o.id = wo.order_id
          LEFT JOIN agents a
              ON a.id = o.original_agent_id
          LEFT JOIN resellers r
              ON r.id = o.reseller_id
          LEFT JOIN order_services os
              ON os.order_id = o.id
             AND os.deleted_at IS NULL
          LEFT JOIN services s
              ON s.id = os.service_id
             AND s.deleted_at IS NULL
          WHERE w.id = :workId
            AND w.deleted_at IS NULL
            AND o.deleted_at IS NULL
          LIMIT 1
          """, nativeQuery = true)
  Optional<WorkDetailForGuideProjection> findWorkDetailByIdForGuide(@Param("workId") Long workId);

  @Query(value = """
        SELECT
            o.id AS orderId,
            rc.name AS contactName,
            o.ref1 AS ref1,
            o.ref2 AS ref2,
            o.is_private AS isPrivate,
            o.adult_count AS adultCount,
            o.child_count AS childCount,
            o.total_fee_amount AS totalFeeAmount,
            oss.label AS status,
            o.guest_group_notes AS notes,
            w.tour_date AS tourDate,
            w.tour_start_time AS tourStartTime,
            w.tour_end_time AS tourEndTime,
            s.name AS serviceName
        FROM works w
        JOIN work_orders wo
            ON wo.work_id = w.id
        JOIN orders o
            ON o.id = wo.order_id
        JOIN order_statuses oss
            ON oss.id = o.status_id
        LEFT JOIN reseller_contacts rc
            ON rc.id = o.pic_contact_id
        LEFT JOIN order_services os
            ON os.order_id = o.id
        AND os.deleted_at IS NULL
        LEFT JOIN services s
            ON s.id = os.service_id
        AND s.deleted_at IS NULL
        WHERE w.id = :workId
        AND w.deleted_at IS NULL
        AND o.deleted_at IS NULL
        ORDER BY o.id
        """, nativeQuery = true)
    List<WorkOrderForGuideProjection> findOrdersByWorkIdForGuide(
        @Param("workId") Long workId);

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
        
        ORDER BY a.id ASC
      """)
  List<WorkGuideDetailProjection> findGuidesByWorkId(
      @Param("workId") Long workId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Work w
            SET w.status = :status,
                w.updatedAt = CURRENT_TIMESTAMP
            WHERE w.id = :id
            AND w.deletedAt IS NULL
        """)
    int updateStatus(
        @Param("id") Long id,
        @Param("status") String status
    );

    @Query(value = """
    SELECT
        o.id AS orderId,
        o.adult_count AS adultCount,
        o.child_count AS childCount,
        o.guest_group_notes AS guestGroupNotes,
        o.leader_phone AS leaderPhone,
        ROUND(AVG(og.age), 1) AS averageAge
    FROM works w
    JOIN work_orders wo
        ON wo.work_id = w.id
    JOIN orders o
        ON o.id = wo.order_id
    LEFT JOIN order_guests og
        ON og.order_id = o.id
    WHERE w.id = :workId
      AND w.deleted_at IS NULL
      AND o.deleted_at IS NULL
    GROUP BY
        o.id,
        o.adult_count,
        o.child_count,
        o.guest_group_notes,
        o.leader_phone
    ORDER BY o.id
    """, nativeQuery = true)
    List<WorkOrderGuestProjection> findOrdersWithAverageAge(
        @Param("workId") Long workId);

    @Query(value = """
    SELECT
        og.id AS id,
        o.id AS orderId,
        og.first_name AS firstName,
        og.last_name AS lastName,
        og.phone_number AS phoneNumber,
        og.age AS age,
        og.gender AS gender,
        og.allergies AS allergies,
        og.special_occasion AS specialOccasion,
        og.dietary_restrictions AS dietaryRestrictions
    FROM works w
    JOIN work_orders wo
        ON wo.work_id = w.id
    JOIN orders o
        ON o.id = wo.order_id
    JOIN order_guests og
        ON og.order_id = o.id
    WHERE w.id = :workId
      AND w.deleted_at IS NULL
      AND o.deleted_at IS NULL
    ORDER BY o.id, og.id
    """, nativeQuery = true)
    List<GuestProjection> findGuestsByWorkId(
        @Param("workId") Long workId);
}
