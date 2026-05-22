package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Especialista;
import com.springboot.proyectospring.model.Paquete;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CitaService {

    private final PaqueteService paqueteService;
    private final EspecialistaService especialistaService;

    public CitaService(PaqueteService paqueteService, EspecialistaService especialistaService) {
        this.paqueteService = paqueteService;
        this.especialistaService = especialistaService;
    }

    public Map<String, Object> confirmarReserva(ReservaRequest request) {
        Optional<Paquete> paquete = paqueteService.buscarPorId(request.getPaqueteId());
        Optional<Especialista> especialista = especialistaService.buscarPorId(request.getEspecialistaId());

        Map<String, Object> confirmacion = new HashMap<>();
        confirmacion.put("paquete", paquete.map(Paquete::getNombre).orElse("Paquete"));
        confirmacion.put("especialista", especialista.map(Especialista::getNombre).orElse("Especialista"));
        confirmacion.put("fecha", request.getFecha());
        confirmacion.put("hora", request.getHora());
        confirmacion.put("paciente", request.getNombreCompleto());
        confirmacion.put("correo", request.getCorreo());
        confirmacion.put("tipoComprobante", request.getTipoComprobante());
        confirmacion.put("documento", "factura".equalsIgnoreCase(request.getTipoComprobante())
                ? request.getRuc() : request.getDni());
        confirmacion.put("metodoPago", request.getMetodoPago());
        confirmacion.put("precio", paquete.map(Paquete::getPrecio).orElse(0.0));
        return confirmacion;
    }
}
