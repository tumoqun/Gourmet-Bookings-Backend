package com.example.demo.repository;

import com.example.demo.dto.WorkItineraryStopList;
import com.example.demo.entity.ItineraryStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryStopRepository extends JpaRepository<ItineraryStop, Long> {
    List<ItineraryStop> findByItineraryIdOrderByStopSequenceAsc(Long itineraryId);
    List<ItineraryStop> findBySupplierId(Long supplierId);

    @Query("""
        SELECT
            s.id AS id,
            s.itineraryId AS itineraryId,
            s.supplierId AS supplierId,
            sp.name AS supplierName,
            sp.phone AS supplierPhone,
            s.stopSequence AS stopSequence,
            s.stopType AS stopType,
            s.scheduledTime AS scheduledTime,
            s.estimatedDurationMinutes AS estimatedDurationMinutes,
            s.description AS description,
            s.specialNotes AS specialNotes,
            s.status AS status,
            s.addedBy AS addedBy
        FROM ItineraryStop s
        JOIN Itinerary i ON i.id = s.itineraryId
        JOIN Supplier sp ON sp.id = s.supplierId
        WHERE i.workId = :workId
          AND sp.deletedAt IS NULL
        ORDER BY s.stopSequence
    """)
    List<WorkItineraryStopList> findByWorkId(@Param("workId") Long workId);
}

