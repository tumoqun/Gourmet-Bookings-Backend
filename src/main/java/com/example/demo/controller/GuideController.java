package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Guide;
import com.example.demo.service.GuideService;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
@Slf4j
public class GuideController {
  private final GuideService guideService;

  @GetMapping("/available")
  public ResponseEntity<List<Guide>> getAvailableGuides(@RequestParam String searchText) {
    List<Guide> guides = guideService.getAvailableGuides(searchText).getBody();
    return ResponseEntity.ok(guides);
  }

  @GetMapping("/unavailable")
  public ResponseEntity<List<Guide>> getUnavailableGuides(@RequestParam String searchText) {
    List<Guide> guides = guideService.getUnavailableGuides(searchText).getBody();
    return ResponseEntity.ok(guides);
  }
}
