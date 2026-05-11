package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderService;
import com.example.demo.entity.OrderAdditionalService;
import com.example.demo.entity.OrderSpecialRequest;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.OrderFinancialLine;
import com.example.demo.entity.OrderStatusHistory;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderServiceRepository;
import com.example.demo.repository.OrderFinancialLineRepository;
import com.example.demo.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceRepository orderServiceRepository;
    private final OrderFinancialLineRepository orderFinancialLineRepository;
    private final OrderStatusRepository orderStatusRepository;

    public List<Order> findAllActive() {
        return orderRepository.findAllActive();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> findByStatusId(Long statusId) {
        return orderRepository.findByStatusId(statusId);
    }

    public List<Order> findByResellerId(Long resellerId) {
        return orderRepository.findByResellerIdAndRequestedAtGreaterThanOrderByRequestedAtDesc(resellerId, LocalDateTime.now().minusMonths(12));
    }

    public Order createOrder(Order order) {
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }
        
        if (order.getStatus() == null) {
            OrderStatus defaultStatus = orderStatusRepository.findByCode("requested")
                .orElseThrow(() -> new RuntimeException("Default order status not found"));
            order.setStatus(defaultStatus);
        }

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Created order with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setOrderChannel(order.getOrderChannel());
        existingOrder.setIsTentative(order.getIsTentative());
        existingOrder.setReseller(order.getReseller());
        existingOrder.setPicContact(order.getPicContact());
        existingOrder.setPicEmail(order.getPicEmail());
        existingOrder.setCopyEmail(order.getCopyEmail());
        existingOrder.setOriginalAgent(order.getOriginalAgent());
        existingOrder.setRef1(order.getRef1());
        existingOrder.setRef2(order.getRef2());
        existingOrder.setVoucherNumber(order.getVoucherNumber());
        existingOrder.setGuestEmail(order.getGuestEmail());
        existingOrder.setAdultCount(order.getAdultCount());
        existingOrder.setChildCount(order.getChildCount());
        existingOrder.setDietaryRestrictions(order.getDietaryRestrictions());
        existingOrder.setCurrencyCode(order.getCurrencyCode());
        existingOrder.setTotalFeeAmount(order.getTotalFeeAmount());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Updated order with ID: {}", updatedOrder.getId());
        return updatedOrder;
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setDeletedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        log.info("Soft deleted order with ID: {}", id);
    }

    public Order submitOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        OrderStatus requestedStatus = orderStatusRepository.findByCode("requested")
            .orElseThrow(() -> new RuntimeException("Requested status not found"));

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(order.getStatus());
        history.setToStatus(requestedStatus);
        history.setChangedByUser(order.getCreatedByUser());
        history.setCreatedAt(LocalDateTime.now());

        order.setStatus(requestedStatus);
        order.setSubmittedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Submitted order with ID: {}", id);

        createFinancialLines(order);

        return order;
    }

    public Order cancelOrder(Long id, String note) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        OrderStatus cancelledStatus = orderStatusRepository.findByCode("cancelled")
            .orElseThrow(() -> new RuntimeException("Cancelled status not found"));

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(order.getStatus());
        history.setToStatus(cancelledStatus);
        history.setChangedByUser(order.getCreatedByUser());
        history.setNote(note);
        history.setCreatedAt(LocalDateTime.now());

        order.setStatus(cancelledStatus);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Cancelled order with ID: {}", id);

        return order;
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void createFinancialLines(Order order) {
        if (order.getTotalFeeAmount() != null && order.getTotalFeeAmount().doubleValue() > 0) {
            OrderFinancialLine serviceFee = new OrderFinancialLine();
            serviceFee.setOrder(order);
            serviceFee.setLineType("SERVICE_FEE");
            serviceFee.setDescription("Service Fee");
            serviceFee.setAmount(order.getTotalFeeAmount());
            serviceFee.setCurrencyCode(order.getCurrencyCode());
            serviceFee.setTaxAmount(null);
            serviceFee.setIsTaxIncluded(false);

            orderFinancialLineRepository.save(serviceFee);
            log.info("Created financial line for order ID: {}", order.getId());
        }
    }
}
