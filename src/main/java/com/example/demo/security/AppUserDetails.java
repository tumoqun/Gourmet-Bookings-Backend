package com.example.demo.security;

import com.example.demo.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class AppUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String fullName;
    private final RoleCode role;
    private final Long guideId;
    private final String passwordHash;
    private final boolean active;
    private final Set<Permission> permissions;

    public AppUserDetails(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = RoleCode.fromString(user.getRole().getCode());
        this.guideId = user.getGuideId();
        this.passwordHash = user.getPasswordHash();
        this.active = Boolean.TRUE.equals(user.getIsActive());
        this.permissions = RolePermissionRegistry.permissionsFor(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public boolean isGuide() {
        return role == RoleCode.GUIDE;
    }

    public boolean isAdmin() {
        return role == RoleCode.ADMIN || role == RoleCode.MANAGER;
    }
}
