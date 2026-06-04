package com.example.demo.repository;

import com.example.demo.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
	List<Itinerary> findByWorkIdOrderByDayNumberAsc(Long workId);
}
