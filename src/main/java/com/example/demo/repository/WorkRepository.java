package com.example.demo.repository;

import com.example.demo.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
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
    List<Work> findByDeletedAtIsNull();
}

