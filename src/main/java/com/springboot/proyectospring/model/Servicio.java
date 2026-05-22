package com.springboot.proyectospring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "servicio")
public class Servicio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numeroVisita;
    private String accionMedica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paquete_id")
    private Paquete paquete;

    public Servicio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroVisita() {
        return numeroVisita;
    }

    public void setNumeroVisita(int numeroVisita) {
        this.numeroVisita = numeroVisita;
    }

    public String getAccionMedica() {
        return accionMedica;
    }

    public void setAccionMedica(String accionMedica) {
        this.accionMedica = accionMedica;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }
}
