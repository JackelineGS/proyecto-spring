package com.springboot.proyectospring.dto;

import java.util.ArrayList;
import java.util.List;

public class HistoriaClinicaDetalle {

    private String numHc;
    private String paciente;
    private String numDocumento;
    private String fechaNacimiento;
    private int edad;
    private String sexo;
    private String gradoInstruccion;
    private String ocupacion;
    private String ultimaCita;
    private int numeroHijos;
    private String residencia;
    private String motivoConsulta;
    private String histNinez;
    private String histAdolescencia;
    private String histAdultez;
    private String usoSustancias;
    private List<DiagnosticoItem> diagnosticos = new ArrayList<>();
    private List<VisitaItem> visitas = new ArrayList<>();
    private List<PsicofarmacoItem> psicofarmacos = new ArrayList<>();
    private List<EvaluacionItem> evaluaciones = new ArrayList<>();

    public String getNumHc() {
        return numHc;
    }

    public void setNumHc(String numHc) {
        this.numHc = numHc;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGradoInstruccion() {
        return gradoInstruccion;
    }

    public void setGradoInstruccion(String gradoInstruccion) {
        this.gradoInstruccion = gradoInstruccion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getUltimaCita() {
        return ultimaCita;
    }

    public void setUltimaCita(String ultimaCita) {
        this.ultimaCita = ultimaCita;
    }

    public int getNumeroHijos() {
        return numeroHijos;
    }

    public void setNumeroHijos(int numeroHijos) {
        this.numeroHijos = numeroHijos;
    }

    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
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

    public List<DiagnosticoItem> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<DiagnosticoItem> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public List<VisitaItem> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<VisitaItem> visitas) {
        this.visitas = visitas;
    }

    public List<PsicofarmacoItem> getPsicofarmacos() {
        return psicofarmacos;
    }

    public void setPsicofarmacos(List<PsicofarmacoItem> psicofarmacos) {
        this.psicofarmacos = psicofarmacos;
    }

    public List<EvaluacionItem> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<EvaluacionItem> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public static class DiagnosticoItem {
        private String codCie;
        private String descripcion;

        public DiagnosticoItem(String codCie, String descripcion) {
            this.codCie = codCie;
            this.descripcion = descripcion;
        }

        public String getCodCie() {
            return codCie;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public static class VisitaItem {
        private String fecha;
        private String hora;
        private String tratamiento;
        private String descripcion;

        public VisitaItem(String fecha, String hora, String tratamiento, String descripcion) {
            this.fecha = fecha;
            this.hora = hora;
            this.tratamiento = tratamiento;
            this.descripcion = descripcion;
        }

        public String getFecha() {
            return fecha;
        }

        public String getHora() {
            return hora;
        }

        public String getTratamiento() {
            return tratamiento;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public static class PsicofarmacoItem {
        private String nombre;
        private String dosis;
        private String frecuencia;
        private String fechaInicio;
        private String fechaFin;

        public PsicofarmacoItem(String nombre, String dosis, String frecuencia,
                                String fechaInicio, String fechaFin) {
            this.nombre = nombre;
            this.dosis = dosis;
            this.frecuencia = frecuencia;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDosis() {
            return dosis;
        }

        public String getFrecuencia() {
            return frecuencia;
        }

        public String getFechaInicio() {
            return fechaInicio;
        }

        public String getFechaFin() {
            return fechaFin;
        }
    }

    public static class EvaluacionItem {
        private String nombreEscala;
        private String nivel;
        private String puntaje;
        private String puntajeMax;

        public EvaluacionItem(String nombreEscala, String nivel, String puntaje, String puntajeMax) {
            this.nombreEscala = nombreEscala;
            this.nivel = nivel;
            this.puntaje = puntaje;
            this.puntajeMax = puntajeMax;
        }

        public String getNombreEscala() {
            return nombreEscala;
        }

        public String getNivel() {
            return nivel;
        }

        public String getPuntaje() {
            return puntaje;
        }

        public String getPuntajeMax() {
            return puntajeMax;
        }
    }
}
