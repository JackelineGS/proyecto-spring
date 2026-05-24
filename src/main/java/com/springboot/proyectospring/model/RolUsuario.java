package com.springboot.proyectospring.model;

public enum RolUsuario {
    ADMIN,
    ESPECIALISTA;

    public static RolUsuario fromDb(String rol) {
        if (rol == null || rol.isBlank()) {
            return ESPECIALISTA;
        }
        String normalizado = rol.trim().toLowerCase();
        if (normalizado.contains("admin") || normalizado.equals("administrador")) {
            return ADMIN;
        }
        return ESPECIALISTA;
    }

    public boolean esAdmin() {
        return this == ADMIN;
    }
}
