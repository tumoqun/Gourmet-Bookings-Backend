package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AvailableSupplierProjection;
import com.example.demo.dto.ReceiptRequest;
import com.example.demo.dto.ReceiptResponse;
import com.example.demo.dto.UpdateReceiptRequest;
import com.example.demo.dto.WorkReceiptResponse;
import com.example.demo.entity.Receipt;
import com.example.demo.repository.ItineraryStopRepository;
import com.example.demo.repository.ReceiptRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReceiptService {
  private final ReceiptRepository receiptRepository;
  private final ItineraryStopRepository itineraryStopRepository;

  public List<WorkReceiptResponse> findReceiptsByWorkId(Long workId) {
    return receiptRepository.findReceiptsByWorkId(workId);
  }

  public List<AvailableSupplierProjection> getAvailableSuppliersNoReceipt(Long workId) {
    return itineraryStopRepository.findAvailableSuppliersNoReceipt(workId);
  }

  public Long createReceipt(ReceiptRequest request) {
    Receipt receipt = new Receipt();

    receipt.setAssignmentId(request.getAssignmentId());
    receipt.setSupplierId(request.getSupplierId());
    receipt.setItineraryStopId(request.getItineraryStopId());
    receipt.setReceiptType(
        request.getReceiptType() == null ? "" : request.getReceiptType());
    receipt.setDescription(
        request.getDescription() == null ? "" : request.getDescription());
    receipt.setAmount(request.getAmount());
    receipt.setFee(request.getFee());
    receipt.setTax(request.getTax());
    receipt.setCurrencyCode(
        request.getCurrencyCode() == null
            ? "USD"
            : request.getCurrencyCode());
    receipt.setReceiptDate(request.getReceiptDate());
    receipt.setReceiptTime(request.getReceiptTime());
    receipt.setReceiptNumber(request.getReceiptNumber());
    receipt.setCheckNumber(request.getCheckNumber() != null && request.getCheckNumber());
    receipt.setCategory(request.getCategory());
    receipt.setPaymentMethod(request.getPaymentMethod());
    receipt.setNotes(request.getNotes());
    receipt.setImageUrl(request.getImageUrl());
    receipt.setIsVerified(
        request.getIsVerified() != null && request.getIsVerified());
    receipt.setVerifiedById(request.getVerifiedById());
    receipt.setVerifiedAt(request.getVerifiedAt());
    receipt.setSubmittedBy(request.getSubmittedBy());
    receipt.setCreatedAt(LocalDateTime.now());
    receipt.setUpdatedAt(LocalDateTime.now());

    receiptRepository.save(receipt);

    return receipt.getId();
  }

  public ReceiptResponse getReceiptById(Long id) {
    return receiptRepository.findReceiptById(id)
        .orElseThrow(() -> new RuntimeException("Receipt not found"));
  }

  public void updateReceipt(Long id, UpdateReceiptRequest request) {
    Receipt receipt = receiptRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Receipt not found"));

    receipt.setSupplierId(request.getSupplierId());
    receipt.setItineraryStopId(request.getItineraryStopId());
    receipt.setAmount(request.getAmount());
    receipt.setFee(request.getFee());
    receipt.setTax(request.getTax());
    receipt.setCheckNumber(request.getCheckNumber());
    receipt.setIsVerified(request.getIsVerified());
    receipt.setNotes(request.getNotes());
    receipt.setImageUrl(request.getImageUrl());
    receipt.setUpdatedAt(LocalDateTime.now());
    if (Boolean.TRUE.equals(request.getIsVerified())) {
      receipt.setVerifiedAt(LocalDateTime.now());
      receipt.setVerifiedById(request.getVerifiedById());
    } else {
      receipt.setVerifiedAt(null);
      receipt.setVerifiedById(null);
    }

    receiptRepository.save(receipt);
  }

  public void deleteReceipt(Long id) {
    Receipt receipt = receiptRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new RuntimeException("Receipt not found"));

    receipt.setDeletedAt(LocalDateTime.now());
    receipt.setUpdatedAt(LocalDateTime.now());

    receiptRepository.save(receipt);
  }
}
