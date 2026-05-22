package com.springboot.proyectospring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paquete")
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;
    private Integer cantidadVisitas;
    private String duracionPorVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_paquete_id")
    private TipoPaquete tipoPaquete;

    @JsonIgnore
    @OneToMany(mappedBy = "paquete", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Servicio> servicios = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "paquete", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();

    public Paquete() {
    }

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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidadVisitas() {
        return cantidadVisitas;
    }

    public void setCantidadVisitas(Integer cantidadVisitas) {
        this.cantidadVisitas = cantidadVisitas;
    }

    public String getDuracionPorVisita() {
        return duracionPorVisita;
    }

    public void setDuracionPorVisita(String duracionPorVisita) {
        this.duracionPorVisita = duracionPorVisita;
    }

    public TipoPaquete getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(TipoPaquete tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
}