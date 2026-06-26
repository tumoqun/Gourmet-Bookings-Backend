package com.example.demo.controller;

import com.example.demo.dto.AssignmentListProjection;
import com.example.demo.dto.GuideAssignmentResponse;
import com.example.demo.dto.WorkDetailForGuideProjection;
import com.example.demo.dto.WorkOrderForGuideProjection;
import com.example.demo.dto.WorkOrderGuestResponse;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.WorkService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guide")
@RequiredArgsConstructor
public class GuidePortalController {

    private final AssignmentService assignmentService;
    private final WorkService workService;

    @GetMapping("/assignments")
    @PreAuthorize("hasAuthority('GUIDE_TOURS_READ')")
    public ResponseEntity<List<AssignmentListProjection>> listAssignments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDate,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(assignmentService.listAssignmentsForCurrentUser(requestedDate, status));
    }

    @PostMapping("/assignments/{id}/accept")
    @PreAuthorize("hasAuthority('GUIDE_ASSIGNMENT_RESPOND')")
    public ResponseEntity<GuideAssignmentResponse> acceptAssignment(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.acceptAssignment(id));
    }

    @PostMapping("/assignments/{id}/reject")
    @PreAuthorize("hasAuthority('GUIDE_ASSIGNMENT_RESPOND')")
    public ResponseEntity<GuideAssignmentResponse> rejectAssignment(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        return ResponseEntity.ok(assignmentService.rejectAssignment(id, reason));
    }

    @PostMapping("/assignments/{id}/start-work")
    @PreAuthorize("hasAuthority('GUIDE_WORK_LIFECYCLE')")
    public ResponseEntity<GuideAssignmentResponse> startWork(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.startWork(id));
    }

    @PostMapping("/assignments/{id}/end-work")
    @PreAuthorize("hasAuthority('GUIDE_WORK_LIFECYCLE')")
    public ResponseEntity<GuideAssignmentResponse> endWork(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.endWork(id));
    }

    @GetMapping("/work/{id}")
    @PreAuthorize("hasAuthority('GUIDE_TOURS_READ')")
    public WorkDetailForGuideProjection getWorkByIdForGuide(@PathVariable Long id) {
        return workService.getWorkByIdForGuide(id);
    }

    @GetMapping("/work/{id}/orders")
    @PreAuthorize("hasAuthority('GUIDE_TOURS_READ')")
    public List<WorkOrderForGuideProjection> getWorkOrdersForGuide(@PathVariable Long id) {
        return workService.getOrdersByWorkIdForGuide(id);
    }

    @GetMapping("/work/{id}/guests")
    @PreAuthorize("hasAuthority('GUIDE_TOURS_READ')")
    public List<WorkOrderGuestResponse> getGuestsByWorkForGuide(@PathVariable Long id) {
        return workService.getGuestsByWorkForGuide(id);
    }
}
