package com.springboot.proyectospring.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaqueteEditDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioTotal;
    private Integer totalSesiones;
    private List<PaqueteDetalleItem> detalles = new ArrayList<>();

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Integer getTotalSesiones() {
        return totalSesiones;
    }

    public void setTotalSesiones(Integer totalSesiones) {
        this.totalSesiones = totalSesiones;
    }

    public List<PaqueteDetalleItem> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PaqueteDetalleItem> detalles) {
        this.detalles = detalles;
    }
}
