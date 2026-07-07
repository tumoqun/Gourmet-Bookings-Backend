package com.example.demo.controller;

import com.example.demo.dto.TourEarningsResponse;
import com.example.demo.service.TourEarningsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class TourEarningsController {

    private final TourEarningsService service;

    public TourEarningsController(TourEarningsService service) {
        this.service = service;
    }

    @GetMapping("/tour-earnings")
    public ResponseEntity<?> getTourEarnings(@RequestParam Long workId, @RequestParam Long guideId) {
        Optional<TourEarningsResponse> resp = service.compute(workId, guideId);
        return resp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
