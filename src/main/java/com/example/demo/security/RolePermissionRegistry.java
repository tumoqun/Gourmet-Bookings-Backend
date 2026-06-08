package com.example.demo.security;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class RolePermissionRegistry {

    private static final Map<RoleCode, Set<Permission>> ROLE_PERMISSIONS = Map.of(
            RoleCode.ADMIN, EnumSet.allOf(Permission.class),
            RoleCode.MANAGER, EnumSet.allOf(Permission.class),
            RoleCode.AGENT, EnumSet.of(
                    Permission.AUTH_LOGIN,
                    Permission.ORDERS_READ,
                    Permission.ORDERS_WRITE,
                    Permission.ORDERS_CONFIRM,
                    Permission.ORDER_GUESTS_MANAGE
            ),
            RoleCode.GUIDE, EnumSet.of(
                    Permission.AUTH_LOGIN,
                    Permission.GUIDE_TOURS_READ,
                    Permission.GUIDE_ASSIGNMENT_RESPOND,
                    Permission.GUIDE_WORK_LIFECYCLE,
                    Permission.GUIDE_RECEIPTS_WRITE,
                    Permission.GUIDE_SALARY_READ
            )
    );

    private RolePermissionRegistry() {
    }

    public static Set<Permission> permissionsFor(RoleCode role) {
        return ROLE_PERMISSIONS.getOrDefault(role, Set.of());
    }

    public static Set<String> authorityNamesFor(RoleCode role) {
        return permissionsFor(role).stream()
                .map(Permission::name)
                .collect(Collectors.toSet());
    }
}
