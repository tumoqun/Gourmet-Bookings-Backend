package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
@Slf4j
public class WorksController {
    private final WorkService workService;

    @GetMapping
    public ResponseEntity<Page<WorkListProjection>> getWorks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
      return ResponseEntity.ok(
          workService.getWorks(page, size));
    }

    @GetMapping("/guests")
    public ResponseEntity<WorkGuestSummaryProjection> getGuestSummary() {
        WorkGuestSummaryProjection summary = workService.getGuestSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{id}")
    public WorkDetailProjection getWorkById(@PathVariable Long id) {
      return workService.getWorkById(id);
    }
}

