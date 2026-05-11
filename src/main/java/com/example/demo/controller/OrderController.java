package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllActive();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByNumber(@PathVariable String orderNumber) {
        Optional<Order> order = orderService.findByOrderNumber(orderNumber);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Long statusId) {
        List<Order> orders = orderService.findByStatusId(statusId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/reseller/{resellerId}")
    public ResponseEntity<List<Order>> getOrdersByReseller(@PathVariable Long resellerId) {
        List<Order> orders = orderService.findByResellerId(resellerId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
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
    public ResponseEntity<Order> submitOrder(@PathVariable Long id) {
        try {
            Order submittedOrder = orderService.submitOrder(id);
            return ResponseEntity.ok(submittedOrder);
        } catch (Exception e) {
            log.error("Error submitting order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id, @RequestBody(required = false) String note) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id, note);
            return ResponseEntity.ok(cancelledOrder);
        } catch (Exception e) {
            log.error("Error cancelling order with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
