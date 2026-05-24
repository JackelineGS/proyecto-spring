package com.springboot.proyectospring.dto;

import java.math.BigDecimal;

public class ServicioForm {

    private Long id;
    private String nombre;
    private BigDecimal precioUn;
    private Integer duracion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioUn() {
        return precioUn;
    }

    public void setPrecioUn(BigDecimal precioUn) {
        this.precioUn = precioUn;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}
