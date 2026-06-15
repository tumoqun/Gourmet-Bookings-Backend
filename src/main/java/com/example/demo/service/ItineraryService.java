package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateItineraryStopRequest;
import com.example.demo.dto.CreateItineraryStopResponse;
import com.example.demo.dto.UpdateItineraryStopStatusRequest;
import com.example.demo.dto.WorkItineraryStopList;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.ItineraryStop;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.ItineraryRepository;
import com.example.demo.repository.ItineraryStopRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItineraryService {
  private final ItineraryRepository itineraryRepository;
  private final ItineraryStopRepository itineraryStopRepository;
  private final ServiceRepository serviceRepository;
  private final SupplierRepository supplierRepository;

  public List<Itinerary> findByWorkIdOrderByDayNumberAsc(Long workId) {
    return itineraryRepository.findByWorkIdOrderByDayNumberAsc(workId);
  }

  public List<WorkItineraryStopList> findStopsByWorkId(Long workId) {
    return itineraryStopRepository.findByWorkId(workId);
  }

  private void addSupplierToService(
      Service service,
      Supplier supplier) {

    boolean exists = service.getSuppliers()
        .stream()
        .anyMatch(sp -> sp.getId().equals(supplier.getId()));

    if (!exists) {
      service.getSuppliers().add(supplier);
      serviceRepository.save(service);
    }
  }

  public CreateItineraryStopResponse createItineraryStop(
      CreateItineraryStopRequest request) {

    Itinerary itinerary = itineraryRepository
        .findFirstByWorkIdOrderByDayNumberAsc(request.getWorkId())
        .orElseGet(() -> {
          Itinerary newItinerary = new Itinerary();
          newItinerary.setWorkId(request.getWorkId());
          newItinerary.setDayNumber(1);
          newItinerary.setDayTitle("Day 1");
          return itineraryRepository.save(newItinerary);
        });

    Long supplierId = request.getSupplierId();

    if (supplierId == null) {
      Service service = serviceRepository.findById(request.getServiceId())
          .orElseThrow(() -> new RuntimeException("Service not found"));
      // stopType = other
      if ("other".equalsIgnoreCase(request.getStopType())) {
        if (request.getOtherName() == null || request.getOtherName().isBlank()) {
          throw new RuntimeException("otherName is required when stopType = other");
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getOtherName());
        supplier.setSupplierType("OTHER");
        supplier.setIsActive(true);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());

        supplier = supplierRepository.save(supplier);

        addSupplierToService(service, supplier);

        supplierId = supplier.getId();
      }
      // stopType = additional
      else if ("additional".equalsIgnoreCase(request.getStopType())) {
        Supplier supplier = supplierRepository
            .findFirstBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull(
                "ADDITIONAL")
            .orElseGet(() -> {
              Supplier newSupplier = new Supplier();
              newSupplier.setName("Additional Information");
              newSupplier.setSupplierType("ADDITIONAL");
              newSupplier.setIsActive(true);
              newSupplier.setCreatedAt(LocalDateTime.now());
              newSupplier.setUpdatedAt(LocalDateTime.now());

              return supplierRepository.save(newSupplier);
            });

        addSupplierToService(service, supplier);

        supplierId = supplier.getId();
      }
    }

    Integer maxSequence = itineraryStopRepository
        .findMaxSequenceByItineraryId(itinerary.getId());

    Integer nextSequence = maxSequence == null ? 1 : maxSequence + 1;

    ItineraryStop stop = new ItineraryStop();

    stop.setItineraryId(itinerary.getId());
    stop.setSupplierId(supplierId);
    stop.setStopType(request.getStopType());
    stop.setScheduledTime(request.getScheduledTime());
    stop.setSpecialNotes(request.getSpecialNotes());
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
