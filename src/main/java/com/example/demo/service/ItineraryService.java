package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.WorkItineraryStopList;
import com.example.demo.entity.Itinerary;
import com.example.demo.repository.ItineraryRepository;
import com.example.demo.repository.ItineraryStopRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItineraryService {
  private final ItineraryRepository itineraryRepository;
  private final ItineraryStopRepository itineraryStopRepository;

  public List<Itinerary> findByWorkIdOrderByDayNumberAsc(Long workId) {
    return itineraryRepository.findByWorkIdOrderByDayNumberAsc(workId);
  }

  public List<WorkItineraryStopList> findStopsByWorkId(Long workId) {
    return itineraryStopRepository.findByWorkId(workId);
  }
}
