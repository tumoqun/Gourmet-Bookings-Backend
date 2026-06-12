package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.WorkService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

  private final WorkService workService;

  @PostMapping
  public ResponseEntity<AssignmentResponse> createAssignment(
      @Valid @RequestBody AssignmentRequest request) {

    return ResponseEntity.ok(
        workService.createAssignment(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AssignmentResponse> updateAssignment(
      @PathVariable Long id,
      @RequestBody AssignmentUpdateRequest request) {

    request.setId(id);

    return ResponseEntity.ok(
        workService.updateAssignment(request));
  }
}
