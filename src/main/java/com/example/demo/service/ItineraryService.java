package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateItineraryStopRequest;
import com.example.demo.dto.CreateItineraryStopResponse;
import com.example.demo.dto.UpdateItineraryStopStatusRequest;
import com.example.demo.dto.WorkItineraryStopList;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.ItineraryStop;
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

  public CreateItineraryStopResponse createItineraryStop(
      CreateItineraryStopRequest request) {

    Itinerary itinerary = itineraryRepository
        .findFirstByWorkIdOrderByDayNumberAsc(
            request.getWorkId())
        .orElseGet(() -> {
          Itinerary newItinerary = new Itinerary();
          newItinerary.setWorkId(
              request.getWorkId());
          newItinerary.setDayNumber(1);
          newItinerary.setDayTitle("Day 1");
          return itineraryRepository.save(
              newItinerary);
        });

    Integer nextSequence = itineraryStopRepository.findMaxSequenceByItineraryId(itinerary.getId()) + 1;
    ItineraryStop stop = new ItineraryStop();
    stop.setItineraryId(itinerary.getId());
    stop.setSupplierId(request.getSupplierId());
    stop.setStopType(request.getStopType());
    stop.setScheduledTime(
        request.getScheduledTime());
    stop.setSpecialNotes(
        request.getSpecialNotes());
    stop.setStatus(request.getStatus());
    stop.setStopSequence(nextSequence);
    stop.setAddedBy(request.getAddedBy());
    stop.setCreatedAt(LocalDateTime.now());
    stop.setUpdatedAt(LocalDateTime.now());
    ItineraryStop saved = itineraryStopRepository.save(stop);

    return CreateItineraryStopResponse.builder()
        .id(saved.getId())
        .itineraryId(saved.getItineraryId())
        .supplierId(saved.getSupplierId())
        .stopType(saved.getStopType())
        .status(saved.getStatus())
        .build();
  }

  public void updateItineraryStopStatus(Long stopId, UpdateItineraryStopStatusRequest request) {
    int updated = itineraryStopRepository.updateStatus(stopId, request.getStatus());
    if (updated == 0) {
      throw new RuntimeException("Itinerary stop not found: " + stopId);
    }
}
}
