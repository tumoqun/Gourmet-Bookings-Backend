package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CreateItineraryStopRequest;
import com.example.demo.dto.CreateItineraryStopResponse;
import com.example.demo.dto.UpdateItineraryStopStatusRequest;
import com.example.demo.dto.WorkItineraryStopList;
import com.example.demo.service.ItineraryService;

import jakarta.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/itineraries")
@PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
public class ItineraryController {
  private final ItineraryService itineraryService;

  @GetMapping("/stops/by-work")
  public ResponseEntity<List<WorkItineraryStopList>> getItineraryStopsByWork(@RequestParam Long workId) {
    List<WorkItineraryStopList> stops = itineraryService.findStopsByWorkId(workId);
    return ResponseEntity.ok(stops);
  }

  @PostMapping("/stops")
  public ResponseEntity<CreateItineraryStopResponse> createItineraryStop(
      @Valid @RequestBody CreateItineraryStopRequest request) {

    return ResponseEntity.ok(itineraryService.createItineraryStop(request));
  }

  @PatchMapping("/stops/{stopId}/status")
  public ResponseEntity<Void> updateStatus(
      @PathVariable Long stopId,
      @Valid @RequestBody UpdateItineraryStopStatusRequest request) {

        System.out.println("PATCH HIT");
        
    itineraryService.updateItineraryStopStatus(stopId, request);
    return ResponseEntity.ok().build();
  }
}
