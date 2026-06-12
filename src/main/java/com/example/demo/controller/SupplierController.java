package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Supplier;
import com.example.demo.service.SupplierService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierController {
  private final SupplierService supplierService;

  @GetMapping("/active")
  public ResponseEntity<List<Supplier>> getActiveSuppliers() {
    return ResponseEntity.ok(supplierService.findByIsActiveAndDeletedAtIsNull(true));
  }
}
