package com.springboot.proyectospring.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "especialista")
public class Especialista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String titulo;

    @ElementCollection
    @CollectionTable(name = "especialista_formacion", joinColumns = @JoinColumn(name = "especialista_id"))
    private List<String> formacion = new ArrayList<>();

    private String colegiatura;

    @ElementCollection
    @CollectionTable(name = "especialista_especialidades", joinColumns = @JoinColumn(name = "especialista_id"))
    private List<String> especialidades = new ArrayList<>();

    private String fotoUrl;
    private String descripcionResumen;

    public Especialista() {
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getFormacion() {
        return formacion;
    }

    public void setFormacion(List<String> formacion) {
        this.formacion = formacion;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }


    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getDescripcionResumen() {
        return descripcionResumen;
    }

    public void setDescripcionResumen(String descripcionResumen) {
        this.descripcionResumen = descripcionResumen;
    }

    public String getFormacionJoin() {
        return String.join("||", formacion);
    }

    public String getEspecialidadesJoin() {
        return String.join("||", especialidades);
    }
}

