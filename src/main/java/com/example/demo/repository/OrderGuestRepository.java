package com.example.demo.repository;

import com.example.demo.entity.OrderGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGuestRepository extends JpaRepository<OrderGuest, Long> {
    List<OrderGuest> findByOrderIdIn(List<Long> orderIds);
    List<OrderGuest> findByOrderId(Long orderId);
}
