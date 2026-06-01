package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.DniRespuestaDto;
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
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
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
    public Map<String, Object> confirmarReserva(ReservaRequest request, HttpSession session) {
        Paquete paquete = paqueteService.buscarPorId(request.getPaqueteId())
                .orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado"));
        Empleado empleado = empleadoService.buscarPorId(request.getEmpleadoId())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        // Recuperar datos de RENIEC almacenados en sesión
        DniRespuestaDto dniData = (DniRespuestaDto) session.getAttribute("dniData");
        String dniNumero = (String) session.getAttribute("dniNumero");

        // Si no hay datos en sesión, usar el DNI del request como fallback
        if (dniNumero == null || dniNumero.isEmpty()) {
            dniNumero = request.getDni();
        }

        final String dni = dniNumero;

        Paciente paciente = pacienteRepository.findByNumDocumento(dni)
                .orElseGet(() -> crearPaciente(dni, dniData, request));

        // Actualizar celular y correo si se proporcionaron (pueden haber cambiado)
        if (request.getCelular() != null && !request.getCelular().isEmpty()) {
            paciente.setCelular(request.getCelular());
        }
        if (request.getCorreo() != null && !request.getCorreo().isEmpty()) {
            paciente.setCorreo(request.getCorreo());
        }
        pacienteRepository.save(paciente);

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

        // Determinar nombre completo para la confirmación
        String nombreCompleto = paciente.getNombreCompleto();

        Map<String, Object> confirmacion = new HashMap<>();
        confirmacion.put("paquete", paquete.getNombre());
        confirmacion.put("empleado", empleado.getNombreCompleto());
        confirmacion.put("fecha", request.getFecha());
        confirmacion.put("hora", request.getHora());
        confirmacion.put("paciente", nombreCompleto);
        confirmacion.put("correo", request.getCorreo());
        confirmacion.put("tipoComprobante", request.getTipoComprobante());
        confirmacion.put("documento", "factura".equalsIgnoreCase(request.getTipoComprobante())
                ? request.getRuc() : dni);
        confirmacion.put("metodoPago", request.getMetodoPago());
        confirmacion.put("precio", paquete.getPrecioTotal());
        confirmacion.put("pagoId", pago.getId());
        confirmacion.put("numeroComprobante", pago.getNumeroComprobante());

        enviarCorreoComprobante(pago, paciente, paquete, empleado, request);

        // Limpiar datos de sesión después de uso exitoso
        session.removeAttribute("dniData");
        session.removeAttribute("dniNumero");

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

    private Paciente crearPaciente(String dni, DniRespuestaDto dniData, ReservaRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNumDocumento(dni);

        if (dniData != null) {
            // Usar datos de RENIEC (almacenados en sesión)
            paciente.setNombre(dniData.getNombres());
            paciente.setApellido(dniData.getApellidos());
            paciente.setResidencia(dniData.getResidencia());
            paciente.setSexo(dniData.getSexo());
        } else {
            // Fallback: usar datos del formulario si no hay datos en sesión
            paciente.setNombre(request.getNombre());
            paciente.setApellido(request.getApellido());
        }

        if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
            paciente.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
        }

        paciente.setCelular(request.getCelular());
        paciente.setCorreo(request.getCorreo());
        paciente = pacienteRepository.save(paciente);

        HistoriaClinica hc = new HistoriaClinica();
        hc.setPaciente(paciente);
        hc.setNumHc("HC-" + java.time.Year.now().getValue() + "-" + String.format("%05d", paciente.getId()));
        historiaClinicaRepository.save(hc);

        return paciente;
    }

    public List<String> obtenerHorariosOcupados(Long empleadoId) {
        LocalDate hoy = LocalDate.now();
        List<Cita> citasFuturas = citaRepository.findByEmpleadoIdAndFechaGreaterThanEqual(empleadoId, hoy);
        
        return citasFuturas.stream()
                .filter(c -> c.getFecha() != null && c.getHora() != null)
                .map(c -> c.getFecha().toString() + " " + c.getHora().toString())
                .toList();
    }
}

