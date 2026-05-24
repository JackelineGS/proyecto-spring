package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Cita;
import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.model.HistoriaClinica;
import com.springboot.proyectospring.repository.CitaRepository;
import com.springboot.proyectospring.repository.PacienteRepository;
import com.springboot.proyectospring.repository.HistoriaClinicaRepository;
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
    private final HistoriaClinicaRepository historiaClinicaRepository;

    public CitaService(PaqueteService paqueteService,
                       EmpleadoService empleadoService,
                       PacienteRepository pacienteRepository,
                       CitaRepository citaRepository,
                       HistoriaClinicaRepository historiaClinicaRepository) {
        this.paqueteService = paqueteService;
        this.empleadoService = empleadoService;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.historiaClinicaRepository = historiaClinicaRepository;
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
        confirmacion.put("paciente", request.getNombre() + " " + request.getApellido());
        confirmacion.put("correo", request.getCorreo());
        confirmacion.put("tipoComprobante", request.getTipoComprobante());
        confirmacion.put("documento", "factura".equalsIgnoreCase(request.getTipoComprobante())
                ? request.getRuc() : request.getDni());
        confirmacion.put("metodoPago", request.getMetodoPago());
        confirmacion.put("precio", paquete.getPrecioTotal());
        return confirmacion;
    }

    private Paciente crearPaciente(ReservaRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
            paciente.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
        }
        paciente.setNumDocumento(request.getDni());
        paciente.setCelular(request.getCelular());
        paciente.setCorreo(request.getCorreo());
        paciente = pacienteRepository.save(paciente);
        
        HistoriaClinica hc = new HistoriaClinica();
        hc.setPaciente(paciente);
        hc.setNumHc("HC-" + java.time.Year.now().getValue() + "-" + String.format("%05d", paciente.getId()));
        historiaClinicaRepository.save(hc);
        
        return paciente;
    }
}
