package com.example.demo.service;

import com.example.demo.dto.AuthUserResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.JwtService;
import com.example.demo.security.Permission;
import com.example.demo.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();

            userRepository.findById(principal.getUserId()).ifPresent(user -> {
                user.setLastLoginAt(LocalDateTime.now());
                userRepository.save(user);
            });

            String token = jwtService.generateToken(principal);
            return LoginResponse.builder()
                    .token(token)
                    .user(toAuthUserResponse(principal))
                    .build();
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public AuthUserResponse me() {
        return toAuthUserResponse(SecurityUtils.currentUser());
    }

    private AuthUserResponse toAuthUserResponse(AppUserDetails user) {
        List<String> permissions = user.getPermissions().stream()
                .map(Permission::name)
                .sorted()
                .toList();

        return AuthUserResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .guideId(user.getGuideId())
                .permissions(permissions)
                .build();
    }
}
