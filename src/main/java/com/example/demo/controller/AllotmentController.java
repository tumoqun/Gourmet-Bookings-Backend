package com.example.demo.controller;

import com.example.demo.dto.AllotmentResponse;
import com.example.demo.entity.Allotment;
import com.example.demo.repository.AllotmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/allotments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
public class AllotmentController {

    private final AllotmentRepository allotmentRepository;

    @GetMapping("/date/{serviceDate}")
    public ResponseEntity<List<AllotmentResponse>> getAllotmentsByDate(@PathVariable LocalDate serviceDate) {
        return ResponseEntity.ok(allotmentRepository.findByDate(serviceDate)
                .stream()
                .map(this::toResponse)
                .toList());
    }

    @GetMapping("/service/{serviceId}/date/{serviceDate}")
    public ResponseEntity<List<AllotmentResponse>> getAllotmentsByServiceAndDate(
            @PathVariable Long serviceId,
            @PathVariable LocalDate serviceDate) {
        return ResponseEntity.ok(allotmentRepository.findByServiceAndDate(serviceId, serviceDate)
                .stream()
                .map(this::toResponse)
                .toList());
    }

    private AllotmentResponse toResponse(Allotment allotment) {
        Integer capacityTotal = allotment.getCapacityTotal();
        Integer reservedTotal = allotment.getReservedTotal();
        Integer availableTotal = capacityTotal == null
                ? null
                : Math.max(0, capacityTotal - (reservedTotal == null ? 0 : reservedTotal));

        return new AllotmentResponse(
                allotment.getId(),
                allotment.getService().getId(),
                allotment.getServiceDate(),
                allotment.getStartTime(),
                capacityTotal,
                reservedTotal,
                availableTotal,
                allotment.getStatus());
    }
}
