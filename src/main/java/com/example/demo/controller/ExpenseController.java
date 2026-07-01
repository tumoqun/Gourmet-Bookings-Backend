package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ExpenseProjection;
import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseResponse;
import com.example.demo.dto.UpdateExpenseRequest;
import com.example.demo.service.ExpenseService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
  private final ExpenseService expenseService;

  @GetMapping("/by-work")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<List<ExpenseProjection>> getExpensesByWork(@RequestParam Long workId) {
    List<ExpenseProjection> expenses = expenseService.getExpensesByWorkId(workId);
    return ResponseEntity.ok(expenses);
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request) {
    return ResponseEntity.ok(expenseService.createExpense(request));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<ExpenseProjection> getExpenseById(@PathVariable Long id) {
    return ResponseEntity.ok(expenseService.getExpenseById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<Void> updateExpense(@PathVariable Long id, @RequestBody UpdateExpenseRequest request) {
    expenseService.updateExpense(id, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
    expenseService.deleteExpense(id);
    return ResponseEntity.noContent().build();
  }
}
