package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderSpecialRequestRepository specialRequestRepository;
    private final OrderAdditionalServiceRepository additionalServiceRepository;
    private final WorkRepository workRepository;
    private final AssignmentRepository assignmentRepository;
    private final GuideRepository guideRepository;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = orderService.findAllActiveResponses();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(value -> ResponseEntity.ok(toOrderResponse(value)))
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
        Optional<Order> order = orderService.findByOrderNumber(orderNumber);
        return order.map(value -> ResponseEntity.ok(toOrderResponse(value)))
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable Long statusId) {
        List<Order> orders = orderService.findByStatusId(statusId);
        return ResponseEntity.ok(orders.stream().map(this::toOrderResponse).collect(Collectors.toList()));
    }

    @GetMapping("/reseller/{resellerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByReseller(@PathVariable Long resellerId) {
        List<Order> orders = orderService.findByResellerId(resellerId);
        return ResponseEntity.ok(orders.stream().map(this::toOrderResponse).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(toOrderResponse(createdOrder));
        } catch (Exception e) {
            log.error("Error creating order", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            log.error("Error updating order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<OrderResponse> submitOrder(@PathVariable Long id) {
        try {
            Order submittedOrder = orderService.submitOrder(id);
            return ResponseEntity.ok(toOrderResponse(submittedOrder));
        } catch (Exception e) {
            log.error("Error submitting order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id, @RequestBody(required = false) String note) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id, note);
            return ResponseEntity.ok(toOrderResponse(cancelledOrder));
        } catch (Exception e) {
            log.error("Error cancelling order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/offer")
    public ResponseEntity<OrderResponse> sendOffer(@PathVariable Long id, @RequestBody OfferCreateRequest request) {
        try {
            Order order = orderService.sendOffer(id, request);
            return ResponseEntity.ok(toOrderResponse(order));
        } catch (Exception e) {
            log.error("Error sending offer for order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long id) {
        try {
            Order order = orderService.confirmOrder(id);
            return ResponseEntity.ok(toOrderResponse(order));
        } catch (Exception e) {
            log.error("Error confirming order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // =====================================================
    // Mapping helpers
    // =====================================================

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus() == null ? null : new OrderStatusResponse(
                order.getStatus().getId(),
                order.getStatus().getCode(),
                order.getStatus().getLabel()
        ));
        response.setOrderChannel(order.getOrderChannel());
        response.setIsTentative(order.getIsTentative());
        response.setCreatedByName(order.getCreatedByName());
        response.setPicEmail(order.getPicEmail());
        response.setCopyEmail(order.getCopyEmail());
        response.setRef1(order.getRef1());
        response.setRef2(order.getRef2());
        response.setVoucherNumber(order.getVoucherNumber());
        response.setGuestEmail(order.getGuestEmail());
        response.setAdultCount(order.getAdultCount());
        response.setChildCount(order.getChildCount());
        response.setDietaryRestrictions(order.getDietaryRestrictions());
        response.setCurrencyCode(order.getCurrencyCode());
        response.setTotalFeeAmount(order.getTotalFeeAmount());
        response.setRequestedAt(order.getRequestedAt());
        response.setSubmittedAt(order.getSubmittedAt());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        // Reseller
        if (order.getReseller() != null) {
            Reseller r = order.getReseller();
            response.setReseller(new ResellerResponse(r.getId(), r.getName(), r.getStatus()));
        }

        // PIC Contact
        if (order.getPicContact() != null) {
            ResellerContact c = order.getPicContact();
            ResellerResponse resellerDto = c.getReseller() == null ? null :
                    new ResellerResponse(c.getReseller().getId(), c.getReseller().getName(), c.getReseller().getStatus());
            response.setPicContact(new ResellerContactResponse(c.getId(), resellerDto, c.getName(), c.getEmail(), c.getIsPrimary()));
        }

        // Original Agent
        if (order.getOriginalAgent() != null) {
            Agent a = order.getOriginalAgent();
            ResellerResponse resellerDto = a.getReseller() == null ? null :
                    new ResellerResponse(a.getReseller().getId(), a.getReseller().getName(), a.getReseller().getStatus());
            response.setOriginalAgent(new AgentResponse(a.getId(), resellerDto, a.getName(), a.getEmail()));
        }

        // Order Services (with admin override flag)
        response.setOrderServices(
                orderService.findServicesByOrderId(order.getId()).stream()
                        .map(this::toOrderServiceResponse)
                        .collect(Collectors.toList())
        );

        // Additional Services (pickup / dropoff)
        response.setAdditionalServices(
                additionalServiceRepository.findEnabledByOrderId(order.getId()).stream()
                        .map(this::toAdditionalServiceResponse)
                        .collect(Collectors.toList())
        );

        // Special Requests
        response.setSpecialRequests(
                specialRequestRepository.findByOrderId(order.getId()).stream()
                        .map(osr -> {
                            SpecialRequestType srt = osr.getSpecialRequestType();
                            return new SpecialRequestTypeResponse(srt.getId(), srt.getCode(), srt.getLabel());
                        })
                        .collect(Collectors.toList())
        );

        // Guide — resolved via Works → Assignments → Guide
        response.setGuide(resolveGuideForOrder(order.getId()));

        return response;
    }

    private OrderServiceResponse toOrderServiceResponse(com.example.demo.entity.OrderService os) {
        OrderServiceResponse response = new OrderServiceResponse();
        response.setId(os.getId());
        response.setServiceNameSnapshot(os.getServiceNameSnapshot());
        response.setTargetDate(os.getTargetDate());
        response.setStartTime(os.getStartTime());
        response.setTimeSlotCode(os.getTimeSlotCode());
        response.setTimezone(os.getTimezone());
        response.setArea(toAreaResponse(os.getArea()));
        response.setServiceType(toServiceTypeResponse(os.getServiceType()));
        response.setService(new ServiceResponse(
                os.getService().getId(),
                toAreaResponse(os.getService().getArea()),
                toServiceTypeResponse(os.getService().getServiceType()),
                os.getService().getName(),
                os.getService().getIsPrivateAvailable(),
                os.getService().getIsActive(),
                os.getService().getDurationMinutes()
        ));
        // Admin override fields (V3)
        response.setIsAdminModified(Boolean.TRUE.equals(os.getIsAdminModified()));
        response.setOriginalServiceId(os.getOriginalServiceId());
        response.setOriginalServiceNameSnapshot(os.getOriginalServiceNameSnapshot());
        return response;
    }

    private OrderAdditionalServiceResponse toAdditionalServiceResponse(OrderAdditionalService oas) {
        OrderAdditionalServiceResponse response = new OrderAdditionalServiceResponse();
        response.setId(oas.getId());
        response.setKind(oas.getKind());
        response.setIsEnabled(oas.getIsEnabled());
        response.setLocation(oas.getLocation());
        response.setHandoffText(oas.getHandoffText());
        response.setSuggestedTime(oas.getSuggestedTime());
        response.setFeeAmount(oas.getFeeAmount());
        response.setCurrencyCode(oas.getCurrencyCode());
        if (oas.getServiceType() != null) {
            response.setServiceType(toServiceTypeResponse(oas.getServiceType()));
        }
        if (oas.getDistanceBand() != null) {
            response.setDistanceBand(toDistanceBandResponse(oas.getDistanceBand()));
        }
        return response;
    }

    private String resolveGuideForOrder(Long orderId) {
        return workRepository.findByOrdersIdAndDeletedAtIsNull(orderId).stream()
                .flatMap(work -> assignmentRepository.findByWorkId(work.getId()).stream())
                .filter(a -> a.getDeletedAt() == null)
                .findFirst()
                .flatMap(a -> guideRepository.findById(a.getGuideId()))
                .map(Guide::getFullName)
                .orElse(null);
    }

    private AreaResponse toAreaResponse(com.example.demo.entity.Area area) {
        return area == null ? null : new AreaResponse(area.getId(), area.getCode(), area.getName());
    }

    private ServiceTypeResponse toServiceTypeResponse(com.example.demo.entity.ServiceType serviceType) {
        return serviceType == null ? null : new ServiceTypeResponse(serviceType.getId(), serviceType.getCode(), serviceType.getName());
    }

    private DistanceBandResponse toDistanceBandResponse(com.example.demo.entity.DistanceBand distanceBand) {
        return distanceBand == null ? null : new DistanceBandResponse(
            distanceBand.getId(),
            distanceBand.getLabel(),
            distanceBand.getSortOrder(),
            distanceBand.getFeeAmount()
        );
    }
}
