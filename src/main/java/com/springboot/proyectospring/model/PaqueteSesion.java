package com.springboot.proyectospring.model;

public enum PaqueteSesion {
    UNA(1),
    CUATRO(4),
    DIEZ(10);

    private final int cantidad;

    PaqueteSesion(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public static PaqueteSesion fromCantidad(int cantidad) {
        return switch (cantidad) {
            case 1 -> UNA;
            case 4 -> CUATRO;
            case 10 -> DIEZ;
            default -> throw new IllegalArgumentException("Paquete no válido: " + cantidad);
        };
    }
}
