package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Comprobante;
import com.springboot.proyectospring.model.Cita;
import com.springboot.proyectospring.model.Especialista;
import com.springboot.proyectospring.model.Pago;
import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.repository.CitaRepository;
import com.springboot.proyectospring.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class CitaService {

    private final PaqueteService paqueteService;
    private final EspecialistaService especialistaService;
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;

    public CitaService(PaqueteService paqueteService,
                          EspecialistaService especialistaService,
                          CitaRepository citaRepository,
                          PacienteRepository pacienteRepository) {
        this.paqueteService = paqueteService;
        this.especialistaService = especialistaService;
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Map<String, Object> confirmarReserva(ReservaRequest request) {
        Optional<Paquete> paqueteOpt = paqueteService.buscarPorId(request.getPaqueteId());
        Optional<Especialista> especialistaOpt = especialistaService.buscarPorId(request.getEspecialistaId());

        Paquete paquete = paqueteOpt.orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado"));
        Especialista especialista = especialistaOpt.orElseThrow(() -> new IllegalArgumentException("Especialista no encontrado"));

        // 1) Persistir paciente (Cita no tiene cascade para Paciente)
        Paciente paciente = new Paciente();
        String[] partes = request.getNombreCompleto() == null ? new String[0] : request.getNombreCompleto().trim().split("\\s+");
        paciente.setNombre(partes.length > 0 ? partes[0] : request.getNombreCompleto());
        paciente.setApellido(partes.length > 1 ? partes[partes.length - 1] : "");
        paciente.setDni(request.getDni());
        paciente.setCelular(request.getCelular());
        paciente.setCorreo(request.getCorreo());
        pacienteRepository.save(paciente);

        // 2) Crear cita
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setPaquete(paquete);
        cita.setEspecialistaId(especialista.getId());
        cita.setFecha(request.getFecha() == null ? null : LocalDate.parse(request.getFecha()));
        cita.setHora(request.getHora());
        cita.setNumeroSesion(1);
        cita.setCreadoEn(LocalDateTime.now());

        // 3) Pago y comprobante (via cascada desde Cita)
        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setFecha(LocalDateTime.now());
        pago.setMedioPago(request.getMetodoPago());
        pago.setMonto(paquete.getPrecio());
        cita.agregarPago(pago);

        Comprobante comprobante = new Comprobante();
        comprobante.setCita(cita);
        comprobante.setTipo(request.getTipoComprobante());
        comprobante.setDocumentoCliente("factura".equalsIgnoreCase(request.getTipoComprobante()) ? request.getRuc() : request.getDni());
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setMontoTotal(paquete.getPrecio());
        cita.setComprobante(comprobante);

        // 4) Persistir todo de una sola vez
        citaRepository.save(cita);

        Map<String, Object> confirmacion = new HashMap<>();
        confirmacion.put("paquete", paquete.getNombre());
        confirmacion.put("especialista", especialista.getNombre());
        confirmacion.put("fecha", request.getFecha());
        confirmacion.put("hora", request.getHora());
        confirmacion.put("paciente", request.getNombreCompleto());
        confirmacion.put("correo", request.getCorreo());
        confirmacion.put("tipoComprobante", request.getTipoComprobante());
        confirmacion.put("documento", "factura".equalsIgnoreCase(request.getTipoComprobante()) ? request.getRuc() : request.getDni());
        confirmacion.put("metodoPago", request.getMetodoPago());
        confirmacion.put("precio", paquete.getPrecio());
        confirmacion.put("comprobanteId", comprobante.getId());

        return confirmacion;
    }
}
