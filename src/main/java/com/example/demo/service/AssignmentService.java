package com.example.demo.service;

import com.example.demo.dto.GuideAssignmentResponse;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Guide;
import com.example.demo.entity.Work;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.WorkRepository;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final WorkRepository workRepository;
    private final GuideRepository guideRepository;

    @Transactional(readOnly = true)
    public List<GuideAssignmentResponse> listAssignmentsForCurrentUser() {
        AppUserDetails user = SecurityUtils.currentUser();
        List<Assignment> assignments;

        if (user.isAdmin()) {
            assignments = assignmentRepository.findAllActive();
        } else if (user.isGuide()) {
            if (user.getGuideId() == null) {
                throw new AccessDeniedException("Guide account is not linked to a guide profile");
            }
            assignments = assignmentRepository.findByGuideIdOrderByCreatedAtDesc(user.getGuideId());
        } else {
            throw new AccessDeniedException("Access denied");
        }

        return assignments.stream()
                .filter(assignment -> assignment.getDeletedAt() == null)
                .sorted(Comparator.comparing(Assignment::getCreatedAt).reversed())
                .map(this::toResponse)
                .toList();
    }

    public GuideAssignmentResponse acceptAssignment(Long assignmentId) {
        Assignment assignment = getOwnedAssignment(assignmentId);
        assignment.setStatus("accepted");
        assignment.setAcceptedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());
        return toResponse(assignmentRepository.save(assignment));
    }

    public GuideAssignmentResponse rejectAssignment(Long assignmentId, String reason) {
        Assignment assignment = getOwnedAssignment(assignmentId);
        assignment.setStatus("rejected");
        assignment.setRejectedAt(LocalDateTime.now());
        assignment.setRejectionReason(reason);
        assignment.setUpdatedAt(LocalDateTime.now());
        return toResponse(assignmentRepository.save(assignment));
    }

    public GuideAssignmentResponse startWork(Long assignmentId) {
        Assignment assignment = getOwnedAssignment(assignmentId);
        assignment.setTourStartedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());
        return toResponse(assignmentRepository.save(assignment));
    }

    public GuideAssignmentResponse endWork(Long assignmentId) {
        Assignment assignment = getOwnedAssignment(assignmentId);
        assignment.setTourEndedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());
        return toResponse(assignmentRepository.save(assignment));
    }

    private Assignment getOwnedAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        if (assignment.getDeletedAt() != null) {
            throw new IllegalArgumentException("Assignment not found");
        }

        AppUserDetails user = SecurityUtils.currentUser();
        if (user.isAdmin()) {
            return assignment;
        }
        if (user.isGuide() && user.getGuideId() != null && user.getGuideId().equals(assignment.getGuideId())) {
            return assignment;
        }
        throw new AccessDeniedException("Access denied for assignment");
    }

    private GuideAssignmentResponse toResponse(Assignment assignment) {
        Work work = workRepository.findById(assignment.getWorkId()).orElse(null);
        Guide guide = guideRepository.findById(assignment.getGuideId()).orElse(null);

        return GuideAssignmentResponse.builder()
                .id(assignment.getId())
                .workId(assignment.getWorkId())
                .workNumber(work != null ? work.getWorkNumber() : null)
                .guideId(assignment.getGuideId())
                .guideName(guide != null ? guide.getFullName() : null)
                .status(assignment.getStatus())
                .acceptedAt(assignment.getAcceptedAt())
                .tourStartedAt(assignment.getTourStartedAt())
                .tourEndedAt(assignment.getTourEndedAt())
                .build();
    }
}
