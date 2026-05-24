package com.springboot.proyectospring.dto;

public class PaqueteDetalleItem {

    private Long servicioId;
    private String nombre;
    private Integer cantidad;

    public PaqueteDetalleItem() {
    }

    public PaqueteDetalleItem(Long servicioId, String nombre, Integer cantidad) {
        this.servicioId = servicioId;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
