package com.springboot.proyectospring.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaqueteForm {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioTotal;
    private Integer totalSesiones;
    private List<Long> servicioIds = new ArrayList<>();
    private List<Integer> cantidades = new ArrayList<>();

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

    public List<Long> getServicioIds() {
        return servicioIds;
    }

    public void setServicioIds(List<Long> servicioIds) {
        this.servicioIds = servicioIds != null ? servicioIds : new ArrayList<>();
    }

    public List<Integer> getCantidades() {
        return cantidades;
    }

    public void setCantidades(List<Integer> cantidades) {
        this.cantidades = cantidades != null ? cantidades : new ArrayList<>();
    }
}
