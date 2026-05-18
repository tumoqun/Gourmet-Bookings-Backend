package com.example.demo.repository;

import com.example.demo.entity.OrderGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {
    List<OrderGroup> findByCustomerGroupIdAndDeletedAtIsNull(Long customerGroupId);
    List<OrderGroup> findByResellerIdAndDeletedAtIsNull(Long resellerId);
    List<OrderGroup> findByDeletedAtIsNull();
}

