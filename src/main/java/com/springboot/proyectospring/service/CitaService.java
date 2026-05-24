package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Cita;
import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.repository.CitaRepository;
import com.springboot.proyectospring.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CitaService {

    private final PaqueteService paqueteService;
    private final EmpleadoService empleadoService;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;

    public CitaService(PaqueteService paqueteService,
                       EmpleadoService empleadoService,
                       PacienteRepository pacienteRepository,
                       CitaRepository citaRepository) {
        this.paqueteService = paqueteService;
        this.empleadoService = empleadoService;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
    }

    @Transactional
    public Map<String, Object> confirmarReserva(ReservaRequest request) {
        Paquete paquete = paqueteService.buscarPorId(request.getPaqueteId())
                .orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado"));
        Empleado empleado = empleadoService.buscarPorId(request.getEmpleadoId())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        Paciente paciente = pacienteRepository.findByNumDocumento(request.getDni())
                .orElseGet(() -> crearPaciente(request));

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setEmpleado(empleado);
        cita.setPaquete(paquete);
        cita.setFecha(LocalDate.parse(request.getFecha()));
        cita.setHora(LocalTime.parse(request.getHora()));
        cita.setEstado("pendiente");
        citaRepository.save(cita);

        Map<String, Object> confirmacion = new HashMap<>();
        confirmacion.put("paquete", paquete.getNombre());
        confirmacion.put("empleado", empleado.getNombreCompleto());
        confirmacion.put("fecha", request.getFecha());
        confirmacion.put("hora", request.getHora());
        confirmacion.put("paciente", request.getNombreCompleto());
        confirmacion.put("correo", request.getCorreo());
        confirmacion.put("tipoComprobante", request.getTipoComprobante());
        confirmacion.put("documento", "factura".equalsIgnoreCase(request.getTipoComprobante())
                ? request.getRuc() : request.getDni());
        confirmacion.put("metodoPago", request.getMetodoPago());
        confirmacion.put("precio", paquete.getPrecioTotal());
        return confirmacion;
    }

    private Paciente crearPaciente(ReservaRequest request) {
        String[] partes = request.getNombreCompleto().trim().split("\\s+", 2);
        Paciente paciente = new Paciente();
        paciente.setNombre(partes[0]);
        paciente.setApellido(partes.length > 1 ? partes[1] : "");
        paciente.setNumDocumento(request.getDni());
        paciente.setCelular(request.getCelular());
        paciente.setCorreo(request.getCorreo());
        return pacienteRepository.save(paciente);
    }
}
