package com.example.demo.repository;

import com.example.demo.entity.ItineraryStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryStopRepository extends JpaRepository<ItineraryStop, Long> {
    List<ItineraryStop> findByItineraryIdOrderByStopSequenceAsc(Long itineraryId);
    List<ItineraryStop> findBySupplierId(Long supplierId);
}

