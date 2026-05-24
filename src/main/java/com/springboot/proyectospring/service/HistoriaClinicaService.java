package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.DniRespuestaDto;
import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.dto.IcdResultadoDto;
import com.springboot.proyectospring.model.Diagnostico;
import com.springboot.proyectospring.model.Evaluacion;
import com.springboot.proyectospring.model.HistoriaClinica;
import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.model.Psicofarmaco;
import com.springboot.proyectospring.model.Visita;
import com.springboot.proyectospring.repository.DiagnosticoRepository;
import com.springboot.proyectospring.repository.EvaluacionRepository;
import com.springboot.proyectospring.repository.HistoriaClinicaRepository;
import com.springboot.proyectospring.repository.PacienteRepository;
import com.springboot.proyectospring.repository.PsicofarmacoRepository;
import com.springboot.proyectospring.repository.VisitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class HistoriaClinicaService {

    private static final Pattern DNI_REGEX = Pattern.compile("^\\d{8}$");
    private static final Pattern HC_REGEX  = Pattern.compile("^HC-\\d{4}-\\d{5}$", Pattern.CASE_INSENSITIVE);

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final PsicofarmacoRepository psicofarmacoRepository;
    private final VisitaRepository visitaRepository;
    private final IcdApiClient icdApiClient;
    private final DniApiClient dniApiClient;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository,
                                   PacienteRepository pacienteRepository,
                                   DiagnosticoRepository diagnosticoRepository,
                                   EvaluacionRepository evaluacionRepository,
                                   PsicofarmacoRepository psicofarmacoRepository,
                                   VisitaRepository visitaRepository,
                                   IcdApiClient icdApiClient,
                                   DniApiClient dniApiClient) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.pacienteRepository = pacienteRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.psicofarmacoRepository = psicofarmacoRepository;
        this.visitaRepository = visitaRepository;
        this.icdApiClient = icdApiClient;
        this.dniApiClient = dniApiClient;
    }

    // ── Búsqueda ────────────────────────────────────────────────────────────

    public Optional<HistoriaClinicaDetalle> buscarPorDni(String dni) {
        if (!DNI_REGEX.matcher(dni).matches()) return Optional.empty();
        return historiaClinicaRepository.findDetalleByPacienteNumDocumento(dni)
                .map(HistoriaClinicaMapper::toDetalle);
    }

    public Optional<HistoriaClinicaDetalle> buscarPorCodigo(String codigo) {
        if (!HC_REGEX.matcher(codigo).matches()) return Optional.empty();
        return historiaClinicaRepository.findDetalleByNumHc(codigo.toUpperCase())
                .map(HistoriaClinicaMapper::toDetalle);
    }

    public Optional<HistoriaClinicaDetalle> resumenPorPacienteId(Long pacienteId) {
        return historiaClinicaRepository.findByPaciente_Id(pacienteId)
                .map(HistoriaClinicaMapper::toDetalle);
    }

    public boolean esDniValido(String dni)       { return DNI_REGEX.matcher(dni).matches(); }
    public boolean esCodigoValido(String codigo) { return HC_REGEX.matcher(codigo).matches(); }

    // ── API DNI ─────────────────────────────────────────────────────────────

    public DniRespuestaDto consultarDni(String dni) {
        return dniApiClient.consultar(dni);
    }

    // ── API ICD-11 ───────────────────────────────────────────────────────────

    public List<IcdResultadoDto> buscarEnIcd(String texto) {
        return icdApiClient.buscar(texto);
    }

    // ── Escritura: Paciente + Historia ───────────────────────────────────────

    @Transactional
    public HistoriaClinica registrarPacienteConHistoria(Paciente paciente, String motivoConsulta) {
        Paciente pacienteGuardado = pacienteRepository.findByNumDocumento(paciente.getNumDocumento())
                .map(existing -> {
                    if (paciente.getNombre() != null && !paciente.getNombre().isBlank())
                        existing.setNombre(paciente.getNombre());
                    if (paciente.getApellido() != null && !paciente.getApellido().isBlank())
                        existing.setApellido(paciente.getApellido());
                    if (paciente.getResidencia() != null && !paciente.getResidencia().isBlank())
                        existing.setResidencia(paciente.getResidencia());
                    if (paciente.getCelular() != null && !paciente.getCelular().isBlank())
                        existing.setCelular(paciente.getCelular());
                    if (paciente.getCorreo() != null && !paciente.getCorreo().isBlank())
                        existing.setCorreo(paciente.getCorreo());
                    if (paciente.getFechaNacimiento() != null)
                        existing.setFechaNacimiento(paciente.getFechaNacimiento());
                    if (paciente.getSexo() != null && !paciente.getSexo().isBlank())
                        existing.setSexo(paciente.getSexo());
                    if (paciente.getGradoInstruccion() != null && !paciente.getGradoInstruccion().isBlank())
                        existing.setGradoInstruccion(paciente.getGradoInstruccion());
                    if (paciente.getOcupacion() != null && !paciente.getOcupacion().isBlank())
                        existing.setOcupacion(paciente.getOcupacion());
                    if (paciente.getNumHijos() != null)
                        existing.setNumHijos(paciente.getNumHijos());
                    return pacienteRepository.save(existing);
                })
                .orElseGet(() -> pacienteRepository.save(paciente));

        Optional<HistoriaClinica> existente = historiaClinicaRepository.findByPaciente_Id(pacienteGuardado.getId());
        if (existente.isPresent()) {
            return existente.get();
        }

        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(pacienteGuardado);
        historia.setNumHc(generarNumHc());
        historia.setMotivoConsulta(motivoConsulta);
        return historiaClinicaRepository.save(historia);
    }

    // ── Escritura: Diagnóstico ───────────────────────────────────────────────

    @Transactional
    public void guardarDiagnostico(String numHc, String codCie, String descripcion) {
        HistoriaClinica historia = buscarEntidadPorCodigo(numHc);
        Diagnostico d = new Diagnostico();
        d.setHistoriaClinica(historia);
        d.setCodCie(codCie);
        d.setDescripcion(descripcion);
        diagnosticoRepository.save(d);
    }

    @Transactional
    public Diagnostico agregarDiagnostico(Long historiaId, String codCie, String descripcion) {
        HistoriaClinica historia = historiaClinicaRepository.findById(historiaId)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada: " + historiaId));
        Diagnostico d = new Diagnostico();
        d.setHistoriaClinica(historia);
        d.setCodCie(codCie);
        d.setDescripcion(descripcion);
        return diagnosticoRepository.save(d);
    }

    // ── Escritura: Anamnesis ─────────────────────────────────────────────────

    @Transactional
    public void actualizarAnamnesis(String numHc, String motivoConsulta,
                                    String histNinez, String histAdolescencia,
                                    String histAdultez, String usoSustancias) {
        HistoriaClinica h = buscarEntidadPorCodigo(numHc);
        h.setMotivoConsulta(motivoConsulta);
        h.setHistNinez(histNinez);
        h.setHistAdolescencia(histAdolescencia);
        h.setHistAdultez(histAdultez);
        h.setUsoSustancias(usoSustancias);
        historiaClinicaRepository.save(h);
    }

    // ── Escritura: Datos del paciente ────────────────────────────────────────

    @Transactional
    public void actualizarPaciente(String numHc, String fechaNacimiento, String sexo,
                                   String celular, String correo, String ocupacion,
                                   String gradoInstruccion, Integer numHijos, String residencia) {
        HistoriaClinica h = buscarEntidadPorCodigo(numHc);
        Paciente p = h.getPaciente();
        if (fechaNacimiento != null && !fechaNacimiento.isBlank())
            p.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
        if (sexo            != null) p.setSexo(sexo);
        if (celular         != null) p.setCelular(celular);
        if (correo          != null) p.setCorreo(correo);
        if (ocupacion       != null) p.setOcupacion(ocupacion);
        if (gradoInstruccion != null) p.setGradoInstruccion(gradoInstruccion);
        if (numHijos        != null) p.setNumHijos(numHijos);
        if (residencia      != null) p.setResidencia(residencia);
        pacienteRepository.save(p);
    }

    // ── Escritura: Psicofármaco ──────────────────────────────────────────────

    @Transactional
    public void agregarPsicofarmaco(String numHc, String nombre, String dosis,
                                    String frecuencia, String fechaInicio, String fechaFin) {
        HistoriaClinica h = buscarEntidadPorCodigo(numHc);
        Psicofarmaco pf = new Psicofarmaco();
        pf.setHistoriaClinica(h);
        pf.setNombre(nombre);
        pf.setDosis(dosis);
        pf.setFrecuencia(frecuencia);
        if (fechaInicio != null && !fechaInicio.isBlank()) pf.setFechaInicio(LocalDate.parse(fechaInicio));
        if (fechaFin    != null && !fechaFin.isBlank())    pf.setFechaFin(LocalDate.parse(fechaFin));
        psicofarmacoRepository.save(pf);
    }

    // ── Escritura: Evaluación ────────────────────────────────────────────────

    @Transactional
    public void agregarEvaluacion(String numHc, String nombreEvaluacion, String nivel,
                                  String puntaje, String puntajeMax) {
        HistoriaClinica h = buscarEntidadPorCodigo(numHc);
        Evaluacion e = new Evaluacion();
        e.setHistoriaClinica(h);
        e.setNombreEvaluacion(nombreEvaluacion);
        e.setNivel(nivel);
        if (puntaje    != null && !puntaje.isBlank())    e.setPuntaje(new BigDecimal(puntaje));
        if (puntajeMax != null && !puntajeMax.isBlank()) e.setPuntajeMax(new BigDecimal(puntajeMax));
        evaluacionRepository.save(e);
    }

    // ── Escritura: Visita ────────────────────────────────────────────────────

    @Transactional
    public void agregarVisita(String numHc, String fecha, String hora,
                              String tratamiento, String descripcion) {
        HistoriaClinica h = buscarEntidadPorCodigo(numHc);
        Visita v = new Visita();
        v.setHistoriaClinica(h);
        if (fecha != null && !fecha.isBlank()) v.setFecha(LocalDate.parse(fecha));
        if (hora  != null && !hora.isBlank())  v.setHora(LocalTime.parse(hora));
        v.setTratamiento(tratamiento);
        v.setDescripcion(descripcion);
        visitaRepository.save(v);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private HistoriaClinica buscarEntidadPorCodigo(String numHc) {
        return historiaClinicaRepository.findByNumHcIgnoreCase(numHc)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada: " + numHc));
    }

    private String generarNumHc() {
        long total = historiaClinicaRepository.count();
        return "HC-" + LocalDate.now().getYear() + "-" + String.format("%05d", total + 1);
    }
}
