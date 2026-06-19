package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
public class WorksController {
  private final WorkService workService;

  @GetMapping
  public ResponseEntity<WorkSearchResponse> getAll(
      @ModelAttribute WorkFilter filter,
      Pageable pageable) {

    return ResponseEntity.ok(
        workService.findAll(filter, pageable));
  }

  @GetMapping("/{id}")
  public WorkDetailProjection getWorkById(@PathVariable Long id) {
    return workService.getWorkById(id);
  }

  @GetMapping("/{workId}/orders")
  public ResponseEntity<List<WorkOrderListResponse>> getWorkOrderListByWorkId(
      @PathVariable Long workId, @RequestParam(required = false) String status) {

    return ResponseEntity.ok(
        workService.getWorkOrderListByWorkId(workId, status));
  }

  @GetMapping("/{id}/guides")
  public ResponseEntity<List<WorkGuideDetailProjection>> getWorkGuidesByWorkId(@PathVariable Long id) {
    List<WorkGuideDetailProjection> guides = workService.getWorkGuidesByWorkId(id);
    return ResponseEntity.ok(guides);
  }
}
