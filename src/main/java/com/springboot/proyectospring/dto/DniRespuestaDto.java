package com.springboot.proyectospring.dto;

public class DniRespuestaDto {

    private String nombres;
    private String apellidos;
    private String residencia;
    private String fechaNacimiento; // formato YYYY-MM-DD (listo para <input type="date">)
    private String sexo;            // "Masculino" | "Femenino"

    public DniRespuestaDto(String nombres, String apellidos, String residencia,
                           String fechaNacimiento, String sexo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.residencia = residencia;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
    }

    public String getNombres()         { return nombres; }
    public String getApellidos()       { return apellidos; }
    public String getResidencia()      { return residencia; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getSexo()            { return sexo; }
}
