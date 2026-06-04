package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Guide;
import com.example.demo.repository.GuideRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GuideService {
  private final GuideRepository guideRepository;

  public ResponseEntity<List<Guide>> getAvailableGuides(String searchText) {
    return ResponseEntity.ok(guideRepository.findByIsActiveTrue(
        searchText.toLowerCase())
                .stream()
                .toList());
  }

  public ResponseEntity<List<Guide>> getUnavailableGuides(String searchText) {
    return ResponseEntity.ok(guideRepository.findByIsActiveFalse(searchText
        .toLowerCase())
        .stream()
        .toList());
  }
}
