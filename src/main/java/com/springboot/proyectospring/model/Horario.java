package com.springboot.proyectospring.model;

public class Horario {

    private Long id;
    private Long especialistaId;
    private String hora;

    public Horario() {
    }

    public Horario(Long id, Long especialistaId, String hora) {
        this.id = id;
        this.especialistaId = especialistaId;
        this.hora = hora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEspecialistaId() {
        return especialistaId;
    }

    public void setEspecialistaId(Long especialistaId) {
        this.especialistaId = especialistaId;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
