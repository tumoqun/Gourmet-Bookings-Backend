package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.WorkReceiptResponse;
import com.example.demo.repository.ReceiptRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReceiptService {
  private final ReceiptRepository receiptRepository;

  public List<WorkReceiptResponse> findReceiptsByWorkId(Long workId) {
    return receiptRepository.findReceiptsByWorkId(workId);
  }
}
