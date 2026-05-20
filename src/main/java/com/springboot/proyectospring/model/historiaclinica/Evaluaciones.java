package com.springboot.proyectospring.model.historiaclinica;

import java.time.LocalDate;
import java.util.Objects;

public class Evaluaciones {

    private Long id;
    private Long historiaClinicaId;

    private LocalDate fecha;

    // Nombre del test (ej. BDI, STAI, etc.)
    private String test;

    // Resultado (texto o puntuación por ahora)
    private String resultado;

    private String interpretacion;
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

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getInterpretacion() {
        return interpretacion;
    }

    public void setInterpretacion(String interpretacion) {
        this.interpretacion = interpretacion;
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
        if (!(o instanceof Evaluaciones that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

