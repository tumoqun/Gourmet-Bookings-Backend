package com.example.demo.repository;

import com.example.demo.entity.PriceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {
    List<PriceEntry> findByPriceBookIdAndDeletedAtIsNull(Long priceBookId);
    List<PriceEntry> findByServiceIdAndDeletedAtIsNull(Long serviceId);
    List<PriceEntry> findByPriceBookIdAndServiceIdAndDeletedAtIsNull(Long priceBookId, Long serviceId);
    List<PriceEntry> findByPriceBookIdAndGuestTypeAndDeletedAtIsNull(Long priceBookId, String guestType);
    List<PriceEntry> findByEffectiveFromLessThanEqualAndEffectiveUntilGreaterThanEqualAndDeletedAtIsNull(LocalDate date1, LocalDate date2);
    List<PriceEntry> findByDeletedAtIsNull();
}

