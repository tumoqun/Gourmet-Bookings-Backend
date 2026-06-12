package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierService {
  private final SupplierRepository supplierRepository;

  public List<Supplier> findByIsActiveAndDeletedAtIsNull(Boolean isActive) {
    return supplierRepository.findByIsActiveAndDeletedAtIsNull(isActive);
  }
}
