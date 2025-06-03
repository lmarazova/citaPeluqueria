package com.example.citaPeluqueria.domain.enums;

public enum Role {
    USER,
    ADMIN,
    MODERATOR,
    GUEST;
    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
