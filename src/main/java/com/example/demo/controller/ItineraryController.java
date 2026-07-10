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
import com.example.demo.dto.AddItineraryNoteRequest;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.ItineraryNote;
import com.example.demo.service.ItineraryService;

import jakarta.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {
  private final ItineraryService itineraryService;

  @GetMapping("/stops/by-work")
  @PreAuthorize("hasAnyAuthority('ASSIGNMENTS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<List<WorkItineraryStopList>> getItineraryStopsByWork(@RequestParam Long workId) {
    List<WorkItineraryStopList> stops = itineraryService.findStopsByWorkId(workId);
    return ResponseEntity.ok(stops);
  }

  @PostMapping("/stops")
  @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
  public ResponseEntity<CreateItineraryStopResponse> createItineraryStop(
      @Valid @RequestBody CreateItineraryStopRequest request) {

    return ResponseEntity.ok(itineraryService.createItineraryStop(request));
  }

  @PatchMapping("/stops/{stopId}/status")
  @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
  public ResponseEntity<Void> updateStatus(
      @PathVariable Long stopId,
      @Valid @RequestBody UpdateItineraryStopStatusRequest request) {

        System.out.println("PATCH HIT");
        
    itineraryService.updateItineraryStopStatus(stopId, request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/by-work/{workId}")
  @PreAuthorize("hasAnyAuthority('ASSIGNMENTS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<Itinerary> getOrCreateItinerary(@PathVariable Long workId) {
    Itinerary itinerary = itineraryService.getOrCreateItinerary(workId);
    return ResponseEntity.ok(itinerary);
  }

  @PostMapping("/{id}/notes")
  @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
  public ResponseEntity<ItineraryNote> addNote(
      @PathVariable Long id,
      @Valid @RequestBody AddItineraryNoteRequest request) {
    ItineraryNote saved = itineraryService.addItineraryNote(id, request.getNoteUrl(), request.getNoteName());
    return ResponseEntity.ok(saved);
  }

  @DeleteMapping("/{id}/notes/{noteId}")
  @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
  public ResponseEntity<Void> deleteNote(
      @PathVariable Long id,
      @PathVariable Long noteId) {
    itineraryService.deleteItineraryNote(id, noteId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/notes/by-work")
  @PreAuthorize("hasAnyAuthority('ASSIGNMENTS_READ', 'GUIDE_TOURS_READ')")
  public ResponseEntity<List<ItineraryNote>> getNotesByWork(@RequestParam Long workId) {
    List<ItineraryNote> notes = itineraryService.findNotesByWorkId(workId);
    return ResponseEntity.ok(notes);
  }
}
