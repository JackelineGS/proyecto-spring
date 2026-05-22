package com.springboot.proyectospring.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.springboot.proyectospring.model.historiaclinica.Anamnesis;
import com.springboot.proyectospring.model.historiaclinica.Diagnostico;
import com.springboot.proyectospring.model.historiaclinica.Evaluaciones;
import com.springboot.proyectospring.model.historiaclinica.Psicofarmaco;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private String motivoConsulta;
    private String observaciones;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "anamnesis_id")
    private Anamnesis anamnesis;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagnostico> diagnosticos = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Psicofarmaco> psicofarmacos = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evaluaciones> evaluaciones = new ArrayList<>();

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

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Anamnesis getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(Anamnesis anamnesis) {
        this.anamnesis = anamnesis;
    }

    public List<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos == null ? new ArrayList<>() : diagnosticos;
    }

    public List<Psicofarmaco> getPsicofarmacos() {
        return psicofarmacos;
    }

    public void setPsicofarmacos(List<Psicofarmaco> psicofarmacos) {
        this.psicofarmacos = psicofarmacos == null ? new ArrayList<>() : psicofarmacos;
    }

    public List<Evaluaciones> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) {
        this.evaluaciones = evaluaciones == null ? new ArrayList<>() : evaluaciones;
    }

    public void agregarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null) {
            this.diagnosticos.add(diagnostico);
        }
    }

    public void agregarPsicofarmaco(Psicofarmaco psicofarmaco) {
        if (psicofarmaco != null) {
            this.psicofarmacos.add(psicofarmaco);
        }
    }

    public void agregarEvaluacion(Evaluaciones evaluacion) {
        if (evaluacion != null) {
            this.evaluaciones.add(evaluacion);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoriaClinica that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
