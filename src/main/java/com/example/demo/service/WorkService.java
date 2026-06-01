package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.WorkDetailProjection;
import com.example.demo.dto.WorkGuestSummaryProjection;
import com.example.demo.dto.WorkListProjection;
import com.example.demo.entity.Work;
import com.example.demo.repository.WorkRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkService {
  private final WorkRepository workRepository;

  public Page<WorkListProjection> getWorks(
      int page,
      int size) {
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.DESC, "id"));

    return workRepository.findAllList(pageable);
  }

  public WorkGuestSummaryProjection getGuestSummary() {
    return workRepository.getGuestSummary();
  }

  public WorkDetailProjection getWorkById(Long id) {
    return workRepository.findWorkDetailById(id)
        .orElseThrow(() -> new RuntimeException("Work not found with id: " + id));
  }
}
