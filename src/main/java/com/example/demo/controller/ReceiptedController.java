package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.WorkReceiptResponse;
import com.example.demo.service.ReceiptService;

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
}
