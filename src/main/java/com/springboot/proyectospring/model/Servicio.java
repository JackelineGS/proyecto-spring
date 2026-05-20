package com.springboot.proyectospring.model;

public class Servicio {

    private Long id;
    private String nombre;
    private String descripcion;
    private TipoServicio tipo;
    private PaqueteSesion paquete;
    private Double precio;
    private String duracion;
    private String formato;
    private String detalle;
    private String imagenUrl;

    public Servicio() {
    }

    public Servicio(Long id, String nombre, String descripcion, TipoServicio tipo, PaqueteSesion paquete,
                    Double precio, String duracion, String formato, String detalle, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.paquete = paquete;
        this.precio = precio;
        this.duracion = duracion;
        this.formato = formato;
        this.detalle = detalle;
        this.imagenUrl = imagenUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicio tipo) {
        this.tipo = tipo;
    }

    public PaqueteSesion getPaquete() {
        return paquete;
    }

    public void setPaquete(PaqueteSesion paquete) {
        this.paquete = paquete;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getEtiquetaPaquete() {
        if (paquete == null) {
            return "";
        }
        return switch (paquete) {
            case UNA -> "1 Sesión";
            case CUATRO -> "Paquete de 4 sesiones";
            case DIEZ -> "Paquete de 10 sesiones";
        };
    }
}
