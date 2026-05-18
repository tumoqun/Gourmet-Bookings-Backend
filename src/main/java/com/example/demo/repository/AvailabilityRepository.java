package com.example.demo.repository;

import com.example.demo.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Optional<Availability> findByGuideIdAndAvailableDate(Long guideId, LocalDate availableDate);
    List<Availability> findByGuideIdAndAvailableDateBetween(Long guideId, LocalDate startDate, LocalDate endDate);
    List<Availability> findByGuideIdAndIsAvailableTrue(Long guideId);
    List<Availability> findByGuideId(Long guideId);
}

