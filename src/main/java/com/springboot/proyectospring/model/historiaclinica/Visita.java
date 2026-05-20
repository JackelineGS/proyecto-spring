package com.springboot.proyectospring.model.historiaclinica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "visita")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Puente: la visita ocurre en el calendario y corresponde a una Cita administrativa.

    private Long citaId;

    // Para el consultorio
    private Long especialistaId;

    private Long historiaClinicaId;


    // Para identificar sesión del paquete
    private int numeroSesion;
    private LocalDate fecha;
    private String hora;

    private LocalDateTime creadaEn;

    // Notas médicas / evolución de esa visita
    private String evolucion;
    private String objetivosTrabajados;
    private String tareasParaCasa;
    private String observaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }

    public Long getEspecialistaId() {
        return especialistaId;
    }

    public void setEspecialistaId(Long especialistaId) {
        this.especialistaId = especialistaId;
    }

    public Long getHistoriaClinicaId() {
        return historiaClinicaId;
    }

    public void setHistoriaClinicaId(Long historiaClinicaId) {
        this.historiaClinicaId = historiaClinicaId;
    }

    public int getNumeroSesion() {
        return numeroSesion;
    }

    public void setNumeroSesion(int numeroSesion) {
        this.numeroSesion = numeroSesion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(LocalDateTime creadaEn) {
        this.creadaEn = creadaEn;
    }

    public String getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
    }

    public String getObjetivosTrabajados() {
        return objetivosTrabajados;
    }

    public void setObjetivosTrabajados(String objetivosTrabajados) {
        this.objetivosTrabajados = objetivosTrabajados;
    }

    public String getTareasParaCasa() {
        return tareasParaCasa;
    }

    public void setTareasParaCasa(String tareasParaCasa) {
        this.tareasParaCasa = tareasParaCasa;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visita visita)) return false;
        return Objects.equals(id, visita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

