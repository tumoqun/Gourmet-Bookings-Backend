package com.example.demo.controller;

import com.example.demo.dto.ConfirmPasswordRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.SalaryScale;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    @GetMapping("/salary-scales")
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<List<SalaryScale>> getAllSalaryScales() {
        return ResponseEntity.ok(userService.getAllSalaryScales());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(search, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDERS_WRITE')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm-password")
    public ResponseEntity<String> confirmPassword(@RequestBody ConfirmPasswordRequest request) {
        userService.confirmPassword(request.getToken(), request.getPassword());
        return ResponseEntity.ok("Password confirmed successfully.");
    }
}
