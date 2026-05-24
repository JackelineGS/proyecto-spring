package com.springboot.proyectospring.dto;

public class CitaPanelDto {

    private int dia;
    private String fecha;
    private String hora;
    private String paciente;
    private String paqueteNombre;
    private String numDocumento;
    private String estado;
    private HistoriaClinicaDetalle historia;

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getPaqueteNombre() {
        return paqueteNombre;
    }

    public void setPaqueteNombre(String paqueteNombre) {
        this.paqueteNombre = paqueteNombre;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public HistoriaClinicaDetalle getHistoria() {
        return historia;
    }

    public void setHistoria(HistoriaClinicaDetalle historia) {
        this.historia = historia;
    }
}
