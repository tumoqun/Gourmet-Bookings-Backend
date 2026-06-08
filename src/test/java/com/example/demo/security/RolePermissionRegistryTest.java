package com.example.demo.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RolePermissionRegistryTest {

    @Test
    void agentCannotDeleteOrOfferOrders() {
        var permissions = RolePermissionRegistry.permissionsFor(RoleCode.AGENT);
        assertTrue(permissions.contains(Permission.ORDERS_READ));
        assertTrue(permissions.contains(Permission.ORDERS_WRITE));
        assertTrue(permissions.contains(Permission.ORDERS_CONFIRM));
        assertFalse(permissions.contains(Permission.ORDERS_DELETE));
        assertFalse(permissions.contains(Permission.ORDERS_OFFER));
        assertFalse(permissions.contains(Permission.ASSIGNMENTS_READ));
    }

    @Test
    void guideHasGuidePermissionsOnly() {
        var permissions = RolePermissionRegistry.permissionsFor(RoleCode.GUIDE);
        assertTrue(permissions.contains(Permission.GUIDE_TOURS_READ));
        assertFalse(permissions.contains(Permission.ORDERS_READ));
    }

    @Test
    void adminHasAllPermissions() {
        var permissions = RolePermissionRegistry.permissionsFor(RoleCode.ADMIN);
        assertTrue(permissions.contains(Permission.VIEW_SWITCH_TOUR_GUIDE));
        assertTrue(permissions.contains(Permission.GUIDE_TOURS_READ));
        assertTrue(permissions.contains(Permission.ORDERS_DELETE));
    }
}
