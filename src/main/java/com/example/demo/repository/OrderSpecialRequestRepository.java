package com.example.demo.repository;

import com.example.demo.entity.OrderSpecialRequest;
import com.example.demo.entity.OrderSpecialRequestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSpecialRequestRepository extends JpaRepository<OrderSpecialRequest, OrderSpecialRequestId> {

    @Query("SELECT osr FROM OrderSpecialRequest osr WHERE osr.order.id = :orderId")
    List<OrderSpecialRequest> findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT DISTINCT osr FROM OrderSpecialRequest osr " +
           "JOIN FETCH osr.specialRequestType srt " +
           "WHERE osr.order.id IN :orderIds")
    List<OrderSpecialRequest> findByOrderIdIn(@Param("orderIds") List<Long> orderIds);
}
