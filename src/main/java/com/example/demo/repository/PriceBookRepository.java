package com.example.demo.repository;

import com.example.demo.entity.PriceBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceBookRepository extends JpaRepository<PriceBook, Long> {
    List<PriceBook> findByIsActiveAndDeletedAtIsNull(Boolean isActive);
    List<PriceBook> findByDeletedAtIsNull();
}

