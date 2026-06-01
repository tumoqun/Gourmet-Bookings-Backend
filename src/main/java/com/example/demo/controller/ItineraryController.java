package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Itinerary;
import com.example.demo.service.ItineraryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {
  private final ItineraryService itineraryService;

  @GetMapping("/by-work")
  public ResponseEntity<List<Itinerary>> getAllItineraries(@RequestParam Long workId) {
    List<Itinerary> itineraries = itineraryService.findByWorkIdOrderByDayNumberAsc(workId);
    return ResponseEntity.ok(itineraries);
  }
}
