package com.example.demo.controller;

import com.example.demo.dto.AgentResponse;
import com.example.demo.dto.ResellerContactResponse;
import com.example.demo.dto.ResellerResponse;
import com.example.demo.entity.Agent;
import com.example.demo.entity.Reseller;
import com.example.demo.entity.ResellerContact;
import com.example.demo.repository.AgentRepository;
import com.example.demo.repository.ResellerContactRepository;
import com.example.demo.repository.ResellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resellers")
@RequiredArgsConstructor
@Slf4j
public class ResellerController {

    private final ResellerRepository resellerRepository;
    private final ResellerContactRepository resellerContactRepository;
    private final AgentRepository agentRepository;

    @GetMapping
    public ResponseEntity<List<ResellerResponse>> getAllResellers() {
        List<ResellerResponse> responses = resellerRepository.findAllOrdered().stream()
                .map(r -> new ResellerResponse(r.getId(), r.getName(), r.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ResellerContactResponse>> getAllContacts() {
        List<ResellerContactResponse> responses = resellerContactRepository.findAll().stream()
                .map(c -> {
                    Reseller r = c.getReseller();
                    ResellerResponse resellerDto = r == null ? null : new ResellerResponse(r.getId(), r.getName(), r.getStatus());
                    return new ResellerContactResponse(c.getId(), resellerDto, c.getName(), c.getEmail(), c.getIsPrimary());
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/agents")
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        List<AgentResponse> responses = agentRepository.findAll().stream()
                .map(a -> {
                    Reseller r = a.getReseller();
                    ResellerResponse resellerDto = r == null ? null : new ResellerResponse(r.getId(), r.getName(), r.getStatus());
                    return new AgentResponse(a.getId(), resellerDto, a.getName(), a.getEmail());
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
