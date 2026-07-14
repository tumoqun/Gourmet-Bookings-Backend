package com.example.demo.service;

import com.example.demo.dto.AssignmentAccountingDetailResponse;
import com.example.demo.dto.TourEarningsResponse;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Guide;
import com.example.demo.entity.SalaryScale;
import com.example.demo.dto.WorkDetailForGuideProjection;
import com.example.demo.entity.Work;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.ReceiptRepository;
import com.example.demo.repository.SalaryScaleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkRepository;
import org.springframework.stereotype.Service;

import com.example.demo.dto.GuideBasicInfo;
import com.example.demo.dto.WorkGuideDetailProjection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TourEarningsService {

    private static final Map<String, String> ASSIGNMENT_STATUS_NAMES = Map.of(
        "pending", "Pending",
        "accepted", "Accepted",
        "rejected", "Rejected",
        "closed", "Closed",
        "completed", "Completed"
    );

    private final AssignmentRepository assignmentRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final SalaryScaleRepository salaryScaleRepository;
    private final ExpenseRepository expenseRepository;
    private final ReceiptRepository receiptRepository;
    private final GuideRepository guideRepository;

    public TourEarningsService(AssignmentRepository assignmentRepository,
                               WorkRepository workRepository,
                               UserRepository userRepository,
                               SalaryScaleRepository salaryScaleRepository,
                               ExpenseRepository expenseRepository,
                               ReceiptRepository receiptRepository,
                               GuideRepository guideRepository) {
        this.assignmentRepository = assignmentRepository;
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        this.salaryScaleRepository = salaryScaleRepository;
        this.expenseRepository = expenseRepository;
        this.receiptRepository = receiptRepository;
        this.guideRepository = guideRepository;
    }

    public Optional<TourEarningsResponse> compute(Long workId, Long guideId) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findByWorkIdAndGuideIdAndDeletedAtIsNull(workId, guideId);
        if (assignmentOpt.isEmpty()) return Optional.empty();
        Assignment assignment = assignmentOpt.get();

        Optional<WorkDetailForGuideProjection> workOpt = workRepository.findWorkDetailByIdForGuide(workId);
        if (workOpt.isEmpty()) return Optional.empty();
        WorkDetailForGuideProjection work = workOpt.get();

        // compute hours earned
        BigDecimal hoursEarned = BigDecimal.ZERO;
        LocalTime start = work.getTourStartTime();
        LocalTime end = work.getTourEndTime();
        if (start != null && end != null) {
            long minutes = Duration.between(start, end).toMinutes();
            hoursEarned = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        }

        // hourly salary lookup from User -> salaryScaleKey -> SalaryScale
        BigDecimal hourlySalary = lookupScaleHourlySalary(guideId);

        BigDecimal taxableSalary = hoursEarned.multiply(hourlySalary).setScale(2, RoundingMode.HALF_UP);

        BigDecimal travelExpenses = expenseRepository.sumAmountByAssignmentId(assignment.getId());
        if (travelExpenses == null) travelExpenses = BigDecimal.ZERO;

        BigDecimal receiptsForTour = receiptRepository.sumAmountByAssignmentId(assignment.getId());
        if (receiptsForTour == null) receiptsForTour = BigDecimal.ZERO;

        TourEarningsResponse resp = new TourEarningsResponse(
            work.getTourDate(),
            work.getTourStartTime(),
            work.getTourEndTime(),
            work.getServiceName(),
            hoursEarned,
            hourlySalary,
            taxableSalary,
            travelExpenses,
            receiptsForTour
        );

        return Optional.of(resp);
    }

    private BigDecimal lookupScaleHourlySalary(Long guideId) {
        var userOpt = userRepository.findByGuideId(guideId);
        if (userOpt.isPresent()) {
            String key = userOpt.get().getSalaryScaleKey();
            if (key != null) {
                Optional<SalaryScale> scaleOpt = salaryScaleRepository.findById(key);
                if (scaleOpt.isPresent()) {
                    return BigDecimal.valueOf(scaleOpt.get().getHourlySalary());
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public Optional<AssignmentAccountingDetailResponse> computeAccountingDetail(Long workId, Long guideId) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findByWorkIdAndGuideIdAndDeletedAtIsNull(workId, guideId);
        if (assignmentOpt.isEmpty()) return Optional.empty();
        Assignment assignment = assignmentOpt.get();

        Optional<WorkDetailForGuideProjection> workOpt = workRepository.findWorkDetailByIdForGuide(workId);
        if (workOpt.isEmpty()) return Optional.empty();
        WorkDetailForGuideProjection work = workOpt.get();

        Optional<Guide> guideOpt = guideRepository.findById(guideId);
        String guideAvatar = guideOpt.map(Guide::getAvatar).orElse(null);
        String guideFullName = guideOpt.map(Guide::getFullName).orElse(null);

        List<GuideBasicInfo> otherGuides = workRepository.findGuidesByWorkId(workId).stream()
            .filter(wg -> !guideId.equals(wg.getGuideId()))
            .map(wg -> GuideBasicInfo.builder()
                .avatar(wg.getAvatar())
                .fullName(wg.getName())
                .build())
            .toList();

        String statusCode = assignment.getStatus();
        String statusName = statusCode != null
            ? ASSIGNMENT_STATUS_NAMES.getOrDefault(statusCode.toLowerCase(), statusCode)
            : null;

        var startTime = assignment.getTourStartedAt() != null
            ? assignment.getTourStartedAt() : work.getTourStartedAt();
        var endTime = assignment.getTourEndedAt() != null
            ? assignment.getTourEndedAt() : work.getTourEndedAt();

        String tourType = Boolean.TRUE.equals(work.getIsPrivateAvailable()) ? "Private" : "Group";
        int adultCount = work.getAdultCount() != null ? work.getAdultCount() : 0;
        int childCount = work.getChildCount() != null ? work.getChildCount() : 0;
        int guestCount = adultCount + childCount;

        int standardMinutes = work.getServiceDurationMinutes() != null ? work.getServiceDurationMinutes() : 0;
        int extraMinutes = assignment.getExtraHoursMinutes() != null ? assignment.getExtraHoursMinutes() : 0;

        BigDecimal standardHoursEarned = BigDecimal.valueOf(standardMinutes)
            .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal extraHoursEarned = BigDecimal.valueOf(extraMinutes)
            .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal tourHoursEarned = standardHoursEarned.add(extraHoursEarned);

        BigDecimal hourlySalary = assignment.getHourlySalaryOverride() != null
            ? assignment.getHourlySalaryOverride() : lookupScaleHourlySalary(guideId);

        BigDecimal taxableSalaryEarned = tourHoursEarned.multiply(hourlySalary).setScale(2, RoundingMode.HALF_UP);

        BigDecimal travelExpenses = expenseRepository.sumAmountByAssignmentId(assignment.getId());
        if (travelExpenses == null) travelExpenses = BigDecimal.ZERO;

        BigDecimal tourReceipts = receiptRepository.sumAmountByAssignmentId(assignment.getId());
        if (tourReceipts == null) tourReceipts = BigDecimal.ZERO;

        return Optional.of(AssignmentAccountingDetailResponse.builder()
            .guideAvatar(guideAvatar)
            .guideFullName(guideFullName)
            .otherGuides(otherGuides)
            .serviceName(work.getServiceName())
            .durationMinutes(work.getServiceDurationMinutes())
            .tourType(tourType)
            .guestCount(guestCount)
            .statusCode(statusCode)
            .statusName(statusName)
            .startTime(startTime)
            .endTime(endTime)
            .standardHoursEarned(standardHoursEarned)
            .extraHoursEarned(extraHoursEarned)
            .tourHoursEarned(tourHoursEarned)
            .hourlySalary(hourlySalary)
            .taxableSalaryEarned(taxableSalaryEarned)
            .travelExpenses(travelExpenses)
            .tourReceipts(tourReceipts)
            .build());
    }
}
