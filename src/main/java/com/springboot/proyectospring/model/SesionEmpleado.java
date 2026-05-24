package com.springboot.proyectospring.model;

import java.io.Serializable;

public class SesionEmpleado implements Serializable {

    private Long id;
    private String correo;
    private String nombreCompleto;
    private RolUsuario rol;

    public SesionEmpleado() {
    }

    public SesionEmpleado(Long id, String correo, String nombreCompleto, RolUsuario rol) {
        this.id = id;
        this.correo = correo;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public static SesionEmpleado from(Empleado empleado) {
        RolUsuario rol = RolUsuario.fromDb(empleado.getRol());
        return new SesionEmpleado(empleado.getId(), empleado.getCorreo(),
                empleado.getNombreCompleto(), rol);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
}
