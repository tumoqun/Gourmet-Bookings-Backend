package com.example.demo.repository;

import com.example.demo.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findBySupplierTypeAndIsActiveAndDeletedAtIsNull(String supplierType, Boolean isActive);
    List<Supplier> findByIsActiveAndDeletedAtIsNull(Boolean isActive);
    List<Supplier> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name);
    List<Supplier> findByDeletedAtIsNull();

    Optional<Supplier> findFirstBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull(
            String supplierType);
}

