package com.example.demo.service;

import com.example.demo.dto.TourEarningsResponse;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.SalaryScale;
import com.example.demo.dto.WorkDetailForGuideProjection;
import com.example.demo.entity.Work;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.ReceiptRepository;
import com.example.demo.repository.SalaryScaleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class TourEarningsService {

    private final AssignmentRepository assignmentRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final SalaryScaleRepository salaryScaleRepository;
    private final ExpenseRepository expenseRepository;
    private final ReceiptRepository receiptRepository;

    public TourEarningsService(AssignmentRepository assignmentRepository,
                               WorkRepository workRepository,
                               UserRepository userRepository,
                               SalaryScaleRepository salaryScaleRepository,
                               ExpenseRepository expenseRepository,
                               ReceiptRepository receiptRepository) {
        this.assignmentRepository = assignmentRepository;
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        this.salaryScaleRepository = salaryScaleRepository;
        this.expenseRepository = expenseRepository;
        this.receiptRepository = receiptRepository;
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
        BigDecimal hourlySalary = BigDecimal.ZERO;
        var userOpt = userRepository.findByGuideId(guideId);
        if (userOpt.isPresent()) {
            String key = userOpt.get().getSalaryScaleKey();
            if (key != null) {
                Optional<SalaryScale> scaleOpt = salaryScaleRepository.findById(key);
                if (scaleOpt.isPresent()) {
                    hourlySalary = BigDecimal.valueOf(scaleOpt.get().getHourlySalary());
                }
            }
        }

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
}
