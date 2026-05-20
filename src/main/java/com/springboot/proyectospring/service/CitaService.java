package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Especialista;
import com.springboot.proyectospring.model.Servicio;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CitaService {

    private final ServicioService servicioService;
    private final EspecialistaService especialistaService;

    public CitaService(ServicioService servicioService, EspecialistaService especialistaService) {
        this.servicioService = servicioService;
        this.especialistaService = especialistaService;
    }

    public Map<String, Object> confirmarReserva(ReservaRequest request) {
        Optional<Servicio> servicio = servicioService.buscarPorId(request.getServicioId());
        Optional<Especialista> especialista = especialistaService.buscarPorId(request.getEspecialistaId());

        Map<String, Object> confirmacion = new HashMap<>();
        confirmacion.put("servicio", servicio.map(Servicio::getNombre).orElse("Servicio"));
        confirmacion.put("especialista", especialista.map(Especialista::getNombre).orElse("Especialista"));
        confirmacion.put("fecha", request.getFecha());
        confirmacion.put("hora", request.getHora());
        confirmacion.put("paciente", request.getNombreCompleto());
        confirmacion.put("correo", request.getCorreo());
        confirmacion.put("tipoComprobante", request.getTipoComprobante());
        confirmacion.put("documento", "factura".equalsIgnoreCase(request.getTipoComprobante())
                ? request.getRuc() : request.getDni());
        confirmacion.put("metodoPago", request.getMetodoPago());
        confirmacion.put("precio", servicio.map(Servicio::getPrecio).orElse(0.0));
        return confirmacion;
    }
}
