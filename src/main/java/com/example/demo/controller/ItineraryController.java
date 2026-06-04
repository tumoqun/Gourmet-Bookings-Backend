package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.WorkItineraryStopList;
import com.example.demo.service.ItineraryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {
  private final ItineraryService itineraryService;

  @GetMapping("/stops/by-work")
  public ResponseEntity<List<WorkItineraryStopList>> getItineraryStopsByWork(@RequestParam Long workId) {
    List<WorkItineraryStopList> stops = itineraryService.findStopsByWorkId(workId);
    return ResponseEntity.ok(stops);
  }
}
