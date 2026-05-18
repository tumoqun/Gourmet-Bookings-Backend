package com.example.demo.repository;

import com.example.demo.entity.PriceBookResellerLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceBookResellerLinkRepository extends JpaRepository<PriceBookResellerLink, Long> {
    List<PriceBookResellerLink> findByPriceBookIdAndIsActiveTrue(Long priceBookId);
    List<PriceBookResellerLink> findByResellerIdAndIsActiveTrue(Long resellerId);
    Optional<PriceBookResellerLink> findByPriceBookIdAndResellerId(Long priceBookId, Long resellerId);
}

