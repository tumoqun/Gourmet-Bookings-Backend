package com.example.demo.repository;

import com.example.demo.entity.OrderFinancialLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderFinancialLineRepository extends JpaRepository<OrderFinancialLine, Long> {
    
    List<OrderFinancialLine> findByOrderIdAndLineType(@Param("orderId") Long orderId, @Param("lineType") String lineType);
    
    List<OrderFinancialLine> findByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(ofl.amount) FROM OrderFinancialLine ofl WHERE ofl.order.id = :orderId")
    BigDecimal sumAmountByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT ofl FROM OrderFinancialLine ofl WHERE ofl.order.deletedAt IS NULL ORDER BY ofl.order.id, ofl.lineType")
    List<OrderFinancialLine> findAllActive();
}
