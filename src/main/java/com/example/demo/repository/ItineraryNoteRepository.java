package com.example.demo.repository;

import com.example.demo.entity.ItineraryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryNoteRepository extends JpaRepository<ItineraryNote, Long> {
    @Query("""
        SELECT n
        FROM ItineraryNote n
        JOIN Itinerary i ON i.id = n.itineraryId
        WHERE i.workId = :workId
        ORDER BY n.id ASC
    """)
    List<ItineraryNote> findByWorkId(@Param("workId") Long workId);
}
