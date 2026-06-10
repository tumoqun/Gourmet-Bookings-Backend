package com.example.demo.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static AppUserDetails currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            throw new AccessDeniedException("Not authenticated");
        }
        return principal;
    }

    public static AppUserDetails currentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            return null;
        }
        return principal;
    }

    public static void requireGuideOwnership(Long guideId) {
        AppUserDetails user = currentUser();
        if (user.isAdmin()) {
            return;
        }
        if (user.isGuide() && user.getGuideId() != null && user.getGuideId().equals(guideId)) {
            return;
        }
        throw new AccessDeniedException("Access denied for guide resource");
    }
}
