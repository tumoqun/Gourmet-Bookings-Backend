package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ServiceSupplierProjection;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.ServiceRepository;
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
  private final ServiceRepository serviceRepository;

  public List<Supplier> findByIsActiveAndDeletedAtIsNull(Boolean isActive) {
    return supplierRepository.findByIsActiveAndDeletedAtIsNull(isActive);
  }

  public List<ServiceSupplierProjection> getSuppliersByServiceId(Long serviceId, String supplierType) {
    return serviceRepository.findSuppliersByServiceId(serviceId, supplierType);
  }
}
