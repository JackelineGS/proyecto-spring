package com.springboot.proyectospring.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Relaciones: la cita es el puente entre paciente y el paquete comprado.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paquete_id")
    private Paquete paquete;

    // Cuando el servicio es un paquete, genera varias visitas (sesión 1..N).
    // Esto permite modelar "Sesión 1 de 3" incluso sin una tabla Visita persistida aún.
    private int numeroSesion;

    private LocalDate fecha;
    private String hora; // se usa el mismo formato que Horario.hora

    // Especialista asignado para bloquear agenda / agenda clínica.
    private Long especialistaId;

    private LocalDateTime creadoEn;

    // En la práctica puede haber múltiples pagos por cita.
    // Relación bidireccional mapeada por el atributo "cita" en la clase Pago
    @JsonIgnore
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pago> pagos = new ArrayList<>();

    // Relación 1 a 1 bidireccional, el dueño de la FK es el Comprobante
    @JsonIgnore
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Comprobante comprobante;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
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

    public Long getEspecialistaId() {
        return especialistaId;
    }

    public void setEspecialistaId(Long especialistaId) {
        this.especialistaId = especialistaId;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos == null ? new ArrayList<>() : pagos;
    }

    public void agregarPago(Pago pago) {
        if (pago != null) {
            this.pagos.add(pago);
        }
    }

    public double getTotalPagado() {
        return pagos.stream().mapToDouble(p -> p.getMonto() == null ? 0.0 : p.getMonto()).sum();
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cita cita)) return false;
        return Objects.equals(id, cita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
