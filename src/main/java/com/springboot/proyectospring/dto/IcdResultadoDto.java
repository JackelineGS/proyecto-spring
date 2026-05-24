package com.springboot.proyectospring.dto;

public class IcdResultadoDto {

    private String codigo;
    private String nombre;

    public IcdResultadoDto(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
}
