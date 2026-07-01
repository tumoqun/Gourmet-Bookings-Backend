package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.ExpenseProjection;
import com.example.demo.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  @Query("""
          SELECT
              e.id AS id,
              e.name AS name,
              e.amount AS amount,
              e.expenseDate AS expenseDate,
              e.expenseTime AS expenseTime,
              e.notes AS notes,
              e.imageUrl AS imageUrl,
              e.createdAt AS createdAt,
              e.submittedBy AS submittedBy,
              a.id AS assignmentId
          FROM Expense e
          JOIN e.assignment a
          WHERE a.workId = :workId
            AND e.deletedAt IS NULL
      """)
  List<ExpenseProjection> findExpensesByWorkId(
      @Param("workId") Long workId);

  @Query("""
              SELECT
                  e.id AS id,
                  e.name AS name,
                  e.amount AS amount,
                  e.expenseDate AS expenseDate,
                  e.expenseTime AS expenseTime,
                  e.notes AS notes,
                  e.imageUrl AS imageUrl,
                  e.createdAt AS createdAt,
                  e.submittedBy AS submittedBy,
                  a.id AS assignmentId
              FROM Expense e
              JOIN e.assignment a
              WHERE e.id = :id
                AND e.deletedAt IS NULL
          """)
  Optional<ExpenseProjection> findProjectionById(
          @Param("id") Long id);
}
