package com.springboot.proyectospring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historiaclinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_clinica")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "numhc", nullable = false, unique = true, length = 50)
    private String numHc;

    @Column(name = "motivoconsulta", columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(name = "histninez", columnDefinition = "TEXT")
    private String histNinez;

    @Column(name = "histadolescencia", columnDefinition = "TEXT")
    private String histAdolescencia;

    @Column(name = "histadultez", columnDefinition = "TEXT")
    private String histAdultez;

    @Column(name = "usosustancias", columnDefinition = "TEXT")
    private String usoSustancias;

    @Column(name = "histfamiliarmadre", columnDefinition = "TEXT")
    private String histFamiliarMadre;

    @Column(name = "histfamiliarpadre", columnDefinition = "TEXT")
    private String histFamiliarPadre;

    @Column(name = "histfamiliarhermanos", columnDefinition = "TEXT")
    private String histFamiliarHermanos;

    @Column(name = "histfamiliarpareja", columnDefinition = "TEXT")
    private String histFamiliarPareja;

    @Column(name = "histfamiliarhijos", columnDefinition = "TEXT")
    private String histFamiliarHijos;

    @OneToMany(mappedBy = "historiaClinica", fetch = FetchType.LAZY)
    private List<Diagnostico> diagnosticos = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", fetch = FetchType.LAZY)
    private List<Evaluacion> evaluaciones = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", fetch = FetchType.LAZY)
    private List<Psicofarmaco> psicofarmacos = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", fetch = FetchType.LAZY)
    private List<Visita> visitas = new ArrayList<>();

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

    public String getNumHc() {
        return numHc;
    }

    public void setNumHc(String numHc) {
        this.numHc = numHc;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getHistNinez() {
        return histNinez;
    }

    public void setHistNinez(String histNinez) {
        this.histNinez = histNinez;
    }

    public String getHistAdolescencia() {
        return histAdolescencia;
    }

    public void setHistAdolescencia(String histAdolescencia) {
        this.histAdolescencia = histAdolescencia;
    }

    public String getHistAdultez() {
        return histAdultez;
    }

    public void setHistAdultez(String histAdultez) {
        this.histAdultez = histAdultez;
    }

    public String getUsoSustancias() {
        return usoSustancias;
    }

    public void setUsoSustancias(String usoSustancias) {
        this.usoSustancias = usoSustancias;
    }

    public String getHistFamiliarMadre() {
        return histFamiliarMadre;
    }

    public void setHistFamiliarMadre(String histFamiliarMadre) {
        this.histFamiliarMadre = histFamiliarMadre;
    }

    public String getHistFamiliarPadre() {
        return histFamiliarPadre;
    }

    public void setHistFamiliarPadre(String histFamiliarPadre) {
        this.histFamiliarPadre = histFamiliarPadre;
    }

    public String getHistFamiliarHermanos() {
        return histFamiliarHermanos;
    }

    public void setHistFamiliarHermanos(String histFamiliarHermanos) {
        this.histFamiliarHermanos = histFamiliarHermanos;
    }

    public String getHistFamiliarPareja() {
        return histFamiliarPareja;
    }

    public void setHistFamiliarPareja(String histFamiliarPareja) {
        this.histFamiliarPareja = histFamiliarPareja;
    }

    public String getHistFamiliarHijos() {
        return histFamiliarHijos;
    }

    public void setHistFamiliarHijos(String histFamiliarHijos) {
        this.histFamiliarHijos = histFamiliarHijos;
    }

    public List<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public List<Psicofarmaco> getPsicofarmacos() {
        return psicofarmacos;
    }

    public void setPsicofarmacos(List<Psicofarmaco> psicofarmacos) {
        this.psicofarmacos = psicofarmacos;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }
}
