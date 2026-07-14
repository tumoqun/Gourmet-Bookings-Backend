package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.RoleResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.entity.Guide;
import com.example.demo.entity.Agent;
import com.example.demo.entity.Reseller;
import com.example.demo.entity.SalaryScale;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.AgentRepository;
import com.example.demo.repository.ResellerRepository;
import com.example.demo.repository.SalaryScaleRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GuideRepository guideRepository;
    private final AgentRepository agentRepository;
    private final ResellerRepository resellerRepository;
    private final SalaryScaleRepository salaryScaleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<SalaryScale> getAllSalaryScales() {
        return salaryScaleRepository.findAll();
    }

    public Page<UserResponse> getAllUsers(String search, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> usersPage;

        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.trim().toLowerCase() + "%";
            usersPage = userRepository.searchUsers(searchPattern, sortedPageable);
        } else {
            usersPage = userRepository.findAll(sortedPageable);
        }

        return usersPage.map(this::toUserResponse);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return toUserResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + request.getEmail());
        }

        Role role = roleRepository.findByCode(request.getRole().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + request.getRole()));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setIsActive(false); // Inactive until password is confirmed

        // Dummy password initially, as it's NOT NULL in DB
        user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));

        if ("GUIDE".equals(role.getCode())) {
            // Create guide entry
            Guide guide = new Guide();
            guide.setFullName(request.getFullName());
            guide.setEmail(request.getEmail());
            guide.setIsActive(true);
            guide = guideRepository.save(guide);

            user.setGuideId(guide.getId());
            user.setSalaryScaleKey(request.getSalaryScaleKey());
        } else if ("AGENT".equals(role.getCode())) {
            if (request.getResellerId() == null) {
                throw new IllegalArgumentException("resellerId is required for AGENT users.");
            }
            Reseller reseller = resellerRepository.findById(request.getResellerId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Reseller not found with id: " + request.getResellerId()));

            // Create agent entry
            Agent agent = new Agent();
            agent.setReseller(reseller);
            agent.setName(request.getFullName());
            agent.setEmail(request.getEmail());
            agentRepository.save(agent);
        }

        user = userRepository.save(user);

        // Generate confirmation token and send email
        String token = jwtService.generatePasswordConfirmationToken(user.getEmail());
        String confirmLink = String.format("%s/confirm-password?token=%s", frontendUrl, token);
        emailService.sendConfirmPasswordEmail(user.getEmail(), user.getFullName(), confirmLink);

        return toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Check email uniqueness if email has changed
        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + request.getEmail());
        }

        Role role = roleRepository.findByCode(request.getRole().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + request.getRole()));

        String oldEmail = user.getEmail();
        String oldRoleCode = user.getRole() != null ? user.getRole().getCode() : "";

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(role);

        // Guide role logic
        if ("GUIDE".equals(role.getCode())) {
            // If they weren't a guide previously or guide record doesn't exist, create it
            if (user.getGuideId() == null) {
                Guide guide = guideRepository.findFirstByEmail(user.getEmail()).orElse(null);
                if (guide == null) {
                    guide = new Guide();
                    guide.setFullName(request.getFullName());
                    guide.setEmail(request.getEmail());
                    guide.setIsActive(true);
                    guide = guideRepository.save(guide);
                }
                user.setGuideId(guide.getId());
            } else {
                // Update existing guide profile name/email
                Guide guide = guideRepository.findById(user.getGuideId()).orElse(null);
                if (guide != null) {
                    guide.setFullName(request.getFullName());
                    guide.setEmail(request.getEmail());
                    guideRepository.save(guide);
                }
            }
            user.setSalaryScaleKey(request.getSalaryScaleKey());
        } else {
            // No longer a guide
            user.setGuideId(null);
            user.setSalaryScaleKey(null);
        }

        // Agent role logic
        if ("AGENT".equals(role.getCode())) {
            if (request.getResellerId() == null) {
                throw new IllegalArgumentException("resellerId is required for AGENT users.");
            }
            Reseller reseller = resellerRepository.findById(request.getResellerId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Reseller not found with id: " + request.getResellerId()));

            Agent agent = agentRepository.findFirstByEmail(oldEmail).orElse(null);
            if (agent == null) {
                agent = new Agent();
            }
            agent.setReseller(reseller);
            agent.setName(request.getFullName());
            agent.setEmail(request.getEmail());
            agentRepository.save(agent);
        } else {
            // If they were an agent before and changed to another role, delete/clean up
            if ("AGENT".equals(oldRoleCode)) {
                agentRepository.findFirstByEmail(oldEmail).ifPresent(agentRepository::delete);
            }
        }

        user = userRepository.save(user);
        return toUserResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Cleanup Guide reference
        if (user.getGuideId() != null) {
            Guide guide = guideRepository.findById(user.getGuideId()).orElse(null);
            if (guide != null) {
                guide.setIsActive(false);
                guideRepository.save(guide);
            }
        }

        // Cleanup Agent reference
        if (user.getRole() != null && "AGENT".equals(user.getRole().getCode())) {
            agentRepository.findFirstByEmail(user.getEmail()).ifPresent(agentRepository::delete);
        }

        userRepository.delete(user);
    }

    @Transactional
    public void confirmPassword(String token, String password) {
        try {
            var claims = jwtService.parseClaims(token);
            String purpose = claims.get("purpose", String.class);
            if (!"confirm-password".equals(purpose)) {
                throw new IllegalArgumentException("Invalid token purpose");
            }
            String email = claims.getSubject();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            user.setPasswordHash(passwordEncoder.encode(password));
            user.setIsActive(true);
            userRepository.save(user);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid or expired password confirmation token", ex);
        }
    }

    private UserResponse toUserResponse(User user) {
        RoleResponse roleResponse = null;
        if (user.getRole() != null) {
            roleResponse = new RoleResponse(
                    user.getRole().getId(),
                    user.getRole().getCode(),
                    user.getRole().getName());
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setRole(roleResponse);
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setIsActive(user.getIsActive());
        response.setGuideId(user.getGuideId());
        response.setSalaryScaleKey(user.getSalaryScaleKey());

        if (user.getRole() != null && "AGENT".equals(user.getRole().getCode())) {
            agentRepository.findFirstByEmail(user.getEmail()).ifPresent(agent -> {
                if (agent.getReseller() != null) {
                    response.setResellerId(agent.getReseller().getId());
                }
            });
        }

        return response;
    }
}
