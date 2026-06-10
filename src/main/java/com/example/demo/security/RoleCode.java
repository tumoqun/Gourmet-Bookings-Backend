package com.example.demo.security;

public enum RoleCode {
    ADMIN,
    MANAGER,
    AGENT,
    GUIDE;

    public static RoleCode fromString(String code) {
        return RoleCode.valueOf(code.toUpperCase());
    }
}
