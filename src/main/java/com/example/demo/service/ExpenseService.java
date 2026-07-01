package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ExpenseProjection;
import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseResponse;
import com.example.demo.dto.UpdateExpenseRequest;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Expense;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExpenseService {
  private final ExpenseRepository expenseRepository;
  private final AssignmentRepository assignmentRepository;

  public List<ExpenseProjection> getExpensesByWorkId(Long workId) {
    return expenseRepository.findExpensesByWorkId(workId);
  }

  public ExpenseResponse createExpense(ExpenseRequest request) {
    Assignment assignment = assignmentRepository
        .findById(request.getAssignmentId())
        .orElseThrow(() -> new RuntimeException("Assignment not found"));
    Expense expense = new Expense();
    expense.setAssignment(assignment);
    expense.setName(request.getName());
    expense.setAmount(request.getAmount());
    expense.setNotes(request.getNotes());
    expense.setImageUrl(request.getImageUrl());
    expense.setExpenseDate(request.getExpenseDate());
    expense.setExpenseTime(request.getExpenseTime());
    expense.setSubmittedBy(request.getSubmittedBy());
    Expense saved = expenseRepository.save(expense);

    return ExpenseResponse.builder()
        .id(saved.getId())
        .name(saved.getName())
        .amount(saved.getAmount())
        .notes(saved.getNotes())
        .assignmentId(saved.getAssignment().getId())
        .expenseDate(saved.getExpenseDate())
        .expenseTime(saved.getExpenseTime())
        .submittedBy(saved.getSubmittedBy())
        .imageUrl(saved.getImageUrl())
        .build();
  }

  public ExpenseProjection getExpenseById(Long id) {
    return expenseRepository.findProjectionById(id)
        .orElseThrow(() -> new RuntimeException("Expense not found"));
  }

  public void updateExpense(Long id, UpdateExpenseRequest request) {
    Expense expense = expenseRepository.findById(id)
        .filter(e -> e.getDeletedAt() == null)
        .orElseThrow(() -> new RuntimeException("Expense not found"));

    Assignment assignment = assignmentRepository
        .findById(request.getAssignmentId())
        .orElseThrow(() -> new RuntimeException("Assignment not found"));

    expense.setName(request.getName());
    expense.setAmount(request.getAmount());
    expense.setNotes(request.getNotes());
    expense.setAssignment(assignment);
    expense.setImageUrl(request.getImageUrl());
    expenseRepository.save(expense);
  }

  public void deleteExpense(Long id) {
    Expense expense = expenseRepository.findById(id)
        .filter(e -> e.getDeletedAt() == null)
        .orElseThrow(() ->
            new RuntimeException("Expense not found"));

    expense.setDeletedAt(LocalDateTime.now());
    expenseRepository.save(expense);
}
}
