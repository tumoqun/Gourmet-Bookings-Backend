package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AvailableSupplierProjection;
import com.example.demo.dto.ReceiptRequest;
import com.example.demo.dto.ReceiptResponse;
import com.example.demo.dto.UpdateReceiptRequest;
import com.example.demo.dto.WorkReceiptResponse;
import com.example.demo.service.ReceiptService;

import jakarta.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/receipts")
public class ReceiptedController {
  private final ReceiptService receiptService;

  @GetMapping("/by-work")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<List<WorkReceiptResponse>> getReceiptsByWork(@RequestParam Long workId) {
    List<WorkReceiptResponse> receipts = receiptService.findReceiptsByWorkId(workId);
    return ResponseEntity.ok(receipts);
  }

  @GetMapping("/available-suppliers/{workId}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<List<AvailableSupplierProjection>> getAvailableSuppliers(@PathVariable Long workId) {
    return ResponseEntity.ok(
        receiptService.getAvailableSuppliersNoReceipt(workId));
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<Long> createReceipt(@Valid @RequestBody ReceiptRequest request) {
    Long id = receiptService.createReceipt(request);
    return ResponseEntity.ok(id);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<ReceiptResponse> getReceiptById(@PathVariable Long id) {
    return ResponseEntity.ok(receiptService.getReceiptById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<Void> updateReceipt(@PathVariable Long id, @RequestBody UpdateReceiptRequest request) {
    receiptService.updateReceipt(id, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('RECEIPTS_OPS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
    receiptService.deleteReceipt(id);
    return ResponseEntity.noContent().build();
  }
}
