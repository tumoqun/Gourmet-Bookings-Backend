package com.example.demo.repository;

import com.example.demo.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
  @Query("""
          SELECT g
          FROM Guide g
          WHERE g.isActive = true
          AND (
              :searchText IS NULL
              OR LOWER(g.fullName) LIKE LOWER(CONCAT('%', :searchText, '%'))
          )
      """)
  List<Guide> findByIsActiveTrue(
      @Param("searchText") String searchText);

  @Query("""
          SELECT g
          FROM Guide g
          WHERE g.isActive = false
          AND (
              :searchText IS NULL
              OR LOWER(g.fullName) LIKE LOWER(CONCAT('%', :searchText, '%'))
          )
      """)
  List<Guide> findByIsActiveFalse(
      @Param("searchText") String searchText);

  @Query("SELECT g.fullName FROM Guide g " +
      "JOIN Assignment a ON g.id = a.guideId " +
      "JOIN Work w ON a.workId = w.id " +
      "JOIN w.orders o " +
      "WHERE o.id = :orderId AND a.deletedAt IS NULL AND w.deletedAt IS NULL")
  Optional<String> findGuideNameByOrderId(@Param("orderId") Long orderId);

  @Query("SELECT o.id, g.fullName FROM Guide g " +
      "JOIN Assignment a ON g.id = a.guideId " +
      "JOIN Work w ON a.workId = w.id " +
      "JOIN w.orders o " +
      "WHERE o.id IN :orderIds AND a.deletedAt IS NULL AND w.deletedAt IS NULL")
  List<Object[]> findGuideNamesByOrderIds(@Param("orderIds") List<Long> orderIds);
}
