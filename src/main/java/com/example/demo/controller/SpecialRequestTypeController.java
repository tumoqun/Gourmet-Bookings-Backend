package com.example.demo.controller;

import com.example.demo.dto.SpecialRequestTypeResponse;
import com.example.demo.repository.SpecialRequestTypeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/special-requests")
public class SpecialRequestTypeController {

    private final SpecialRequestTypeRepository repository;

    public SpecialRequestTypeController(SpecialRequestTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public List<SpecialRequestTypeResponse> getAll() {
        return repository.findAll().stream()
                .map(srt -> new SpecialRequestTypeResponse(srt.getId(), srt.getCode(), srt.getLabel()))
                .collect(Collectors.toList());
    }
}
