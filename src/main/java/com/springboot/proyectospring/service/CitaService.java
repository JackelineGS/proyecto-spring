package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Cita;
import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.model.HistoriaClinica;
import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.model.Pago;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.repository.CitaRepository;
import com.springboot.proyectospring.repository.HistoriaClinicaRepository;
import com.springboot.proyectospring.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CitaService {

    private final PaqueteService paqueteService;
    private final EmpleadoService empleadoService;
    private final PagoService pagoService;
    private final CorreoService correoService;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    public CitaService(PaqueteService paqueteService,
                       EmpleadoService empleadoService,
                       PagoService pagoService,
                       CorreoService correoService,
                       PacienteRepository pacienteRepository,
                       CitaRepository citaRepository,
                       HistoriaClinicaRepository historiaClinicaRepository) {
        this.paqueteService = paqueteService;
        this.empleadoService = empleadoService;
        this.pagoService = pagoService;
        this.correoService = correoService;
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

        Pago pago = pagoService.registrar(
                paciente,
                paquete,
                paquete.getPrecioTotal(),
                request.getTipoComprobante(),
                request.getMetodoPago());

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
        confirmacion.put("pagoId", pago.getId());
        confirmacion.put("numeroComprobante", pago.getNumeroComprobante());

        enviarCorreoComprobante(pago, paciente, paquete, empleado, request);

        return confirmacion;
    }

    private void enviarCorreoComprobante(Pago pago, Paciente paciente, Paquete paquete,
                                         Empleado empleado, ReservaRequest request) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("pagoId",           pago.getId());
        vars.put("numeroComprobante", pago.getNumeroComprobante());
        vars.put("tipoComprobante",   pago.getTipoComprobante());
        vars.put("fechaPago",         pago.getFecha().toString());
        vars.put("pacienteNombre",    paciente.getNombreCompleto());
        vars.put("pacienteDni",       paciente.getNumDocumento());
        vars.put("pacienteCorreo",    paciente.getCorreo() != null ? paciente.getCorreo() : request.getCorreo());
        vars.put("paqueteNombre",     paquete.getNombre());
        vars.put("paqueteSesiones",   paquete.getTotalSesiones());
        vars.put("monto",             pago.getMonto());
        vars.put("empleado",          empleado.getNombreCompleto());
        vars.put("fechaCita",         request.getFecha());
        vars.put("horaCita",          request.getHora());
        vars.put("metodoPago",        pago.getMetodoPago());

        String correo = paciente.getCorreo() != null ? paciente.getCorreo() : request.getCorreo();
        correoService.enviarComprobante(correo, vars);
    }

    private Paciente crearPaciente(ReservaRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty())
            paciente.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
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
