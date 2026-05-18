package com.example.demo.repository;

import com.example.demo.entity.OrderAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAdditionalServiceRepository extends JpaRepository<OrderAdditionalService, Long> {

    @Query("SELECT oas FROM OrderAdditionalService oas WHERE oas.order.id = :orderId AND oas.isEnabled = true")
    List<OrderAdditionalService> findEnabledByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT DISTINCT oas FROM OrderAdditionalService oas " +
           "LEFT JOIN FETCH oas.serviceType st " +
           "WHERE oas.order.id IN :orderIds AND oas.isEnabled = true")
    List<OrderAdditionalService> findEnabledByOrderIdIn(@Param("orderIds") List<Long> orderIds);
}
