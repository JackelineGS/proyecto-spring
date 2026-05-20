package com.springboot.proyectospring.model.historiaclinica;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "anamnesis")
public class Anamnesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long historiaClinicaId;

    private LocalDate fecha;

    // Datos familiares / historia personal (texto libre por ahora)
    private String historiaFamiliar;
    private String historiaPersonal;
    private String antecedentesEscolares;
    private String antecedentesMedicos;
    private String antecedentesPsiquiatricos;

    // Síntomas actuales
    private String sintomasPrincipales;
    private String comentarios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHistoriaClinicaId() {
        return historiaClinicaId;
    }

    public void setHistoriaClinicaId(Long historiaClinicaId) {
        this.historiaClinicaId = historiaClinicaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHistoriaFamiliar() {
        return historiaFamiliar;
    }

    public void setHistoriaFamiliar(String historiaFamiliar) {
        this.historiaFamiliar = historiaFamiliar;
    }

    public String getHistoriaPersonal() {
        return historiaPersonal;
    }

    public void setHistoriaPersonal(String historiaPersonal) {
        this.historiaPersonal = historiaPersonal;
    }

    public String getAntecedentesEscolares() {
        return antecedentesEscolares;
    }

    public void setAntecedentesEscolares(String antecedentesEscolares) {
        this.antecedentesEscolares = antecedentesEscolares;
    }

    public String getAntecedentesMedicos() {
        return antecedentesMedicos;
    }

    public void setAntecedentesMedicos(String antecedentesMedicos) {
        this.antecedentesMedicos = antecedentesMedicos;
    }

    public String getAntecedentesPsiquiatricos() {
        return antecedentesPsiquiatricos;
    }

    public void setAntecedentesPsiquiatricos(String antecedentesPsiquiatricos) {
        this.antecedentesPsiquiatricos = antecedentesPsiquiatricos;
    }

    public String getSintomasPrincipales() {
        return sintomasPrincipales;
    }

    public void setSintomasPrincipales(String sintomasPrincipales) {
        this.sintomasPrincipales = sintomasPrincipales;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Anamnesis anamnesis)) return false;
        return Objects.equals(id, anamnesis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

