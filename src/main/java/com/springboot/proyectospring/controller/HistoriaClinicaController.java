package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.DniRespuestaDto;
import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.dto.IcdResultadoDto;
import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.service.HistoriaClinicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/especialista/historias")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    public HistoriaClinicaController(HistoriaClinicaService historiaClinicaService) {
        this.historiaClinicaService = historiaClinicaService;
    }

    // ── Consulta existente ───────────────────────────────────────────────────

    @GetMapping("/detalle/{codigo}")
    public String detalle(@PathVariable String codigo, Model model) {
        HistoriaClinicaDetalle historia = historiaClinicaService.buscarPorCodigo(codigo).orElse(null);
        model.addAttribute("historia", historia);
        return "especialista/fragments/historia-modal :: modal";
    }

    @GetMapping("/api/detalle/{codigo}")
    @ResponseBody
    public HistoriaClinicaDetalle detalleApi(@PathVariable String codigo) {
        return historiaClinicaService.buscarPorCodigo(codigo).orElse(null);
    }

    // ── Registro de nuevo paciente + historia ────────────────────────────────

    @GetMapping("/nueva")
    public String nuevaForm() {
        return "especialista/historia-clinica/nueva";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam String numDocumento,
                            @RequestParam String nombre,
                            @RequestParam String apellido,
                            @RequestParam(required = false) String residencia,
                            @RequestParam(required = false) String celular,
                            @RequestParam(required = false) String correo,
                            @RequestParam(required = false) String fechaNacimiento,
                            @RequestParam(required = false) String sexo,
                            @RequestParam(required = false) String gradoInstruccion,
                            @RequestParam(required = false) String ocupacion,
                            @RequestParam(required = false) Integer numHijos,
                            @RequestParam String motivoConsulta,
                            RedirectAttributes redirectAttributes) {
        try {
            Paciente paciente = new Paciente();
            paciente.setNumDocumento(numDocumento.trim());
            paciente.setNombre(nombre.trim());
            paciente.setApellido(apellido.trim());
            paciente.setResidencia(residencia);
            paciente.setCelular(celular);
            paciente.setCorreo(correo);
            if (fechaNacimiento != null && !fechaNacimiento.isBlank())
                paciente.setFechaNacimiento(java.time.LocalDate.parse(fechaNacimiento));
            paciente.setSexo(sexo);
            paciente.setGradoInstruccion(gradoInstruccion);
            paciente.setOcupacion(ocupacion);
            paciente.setNumHijos(numHijos);

            boolean esPacienteNuevo = historiaClinicaService.buscarPorDni(numDocumento.trim()).isEmpty();
            var historia = historiaClinicaService.registrarPacienteConHistoria(paciente, motivoConsulta);
            redirectAttributes.addFlashAttribute("mensaje",
                    esPacienteNuevo
                        ? "Historia clínica " + historia.getNumHc() + " creada correctamente."
                        : "El paciente ya tenía una historia clínica (" + historia.getNumHc() + "). Se actualizaron sus datos.");
            return "redirect:/especialista/historias?tipo=codigo&valor=" + historia.getNumHc();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/especialista/historias/nueva";
        }
    }

    // ── Diagnóstico ──────────────────────────────────────────────────────────

    @PostMapping("/{id}/diagnosticos")
    public String agregarDiagnostico(@PathVariable Long id,
                                     @RequestParam String codCie,
                                     @RequestParam String descripcion,
                                     RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.agregarDiagnostico(id, codCie, descripcion);
            redirectAttributes.addFlashAttribute("mensaje", "Diagnóstico guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias";
    }

    @PostMapping("/diagnostico/guardar")
    public String guardarDiagnostico(@RequestParam String codigo,
                                     @RequestParam String codCie,
                                     @RequestParam String descripcion,
                                     RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.guardarDiagnostico(codigo, codCie, descripcion);
            redirectAttributes.addFlashAttribute("mensaje", "Diagnóstico guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias?tipo=codigo&valor=" + codigo;
    }

    // ── Anamnesis ────────────────────────────────────────────────────────────

    @PostMapping("/anamnesis/guardar")
    public String guardarAnamnesis(@RequestParam String codigo,
                                   @RequestParam(required = false) String motivoConsulta,
                                   @RequestParam(required = false) String histNinez,
                                   @RequestParam(required = false) String histAdolescencia,
                                   @RequestParam(required = false) String histAdultez,
                                   @RequestParam(required = false) String usoSustancias,
                                   RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.actualizarAnamnesis(codigo, motivoConsulta,
                    histNinez, histAdolescencia, histAdultez, usoSustancias);
            redirectAttributes.addFlashAttribute("mensaje", "Anamnesis actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias?tipo=codigo&valor=" + codigo;
    }

    // ── Paciente ─────────────────────────────────────────────────────────────

    @PostMapping("/paciente/guardar")
    public String guardarPaciente(@RequestParam String codigo,
                                  @RequestParam(required = false) String fechaNacimiento,
                                  @RequestParam(required = false) String sexo,
                                  @RequestParam(required = false) String celular,
                                  @RequestParam(required = false) String correo,
                                  @RequestParam(required = false) String ocupacion,
                                  @RequestParam(required = false) String gradoInstruccion,
                                  @RequestParam(required = false) Integer numeroHijos,
                                  @RequestParam(required = false) String residencia,
                                  RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.actualizarPaciente(codigo, fechaNacimiento, sexo,
                    celular, correo, ocupacion, gradoInstruccion, numeroHijos, residencia);
            redirectAttributes.addFlashAttribute("mensaje", "Datos del paciente actualizados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias?tipo=codigo&valor=" + codigo;
    }

    // ── Psicofármaco ─────────────────────────────────────────────────────────

    @PostMapping("/psicofarmaco/guardar")
    public String guardarPsicofarmaco(@RequestParam String codigo,
                                      @RequestParam String nombre,
                                      @RequestParam(required = false) String dosis,
                                      @RequestParam(required = false) String frecuencia,
                                      @RequestParam(required = false) String fechaInicio,
                                      @RequestParam(required = false) String fechaFin,
                                      RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.agregarPsicofarmaco(codigo, nombre, dosis, frecuencia, fechaInicio, fechaFin);
            redirectAttributes.addFlashAttribute("mensaje", "Psicofármaco registrado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias?tipo=codigo&valor=" + codigo;
    }

    // ── Evaluación ───────────────────────────────────────────────────────────

    @PostMapping("/evaluacion/guardar")
    public String guardarEvaluacion(@RequestParam String codigo,
                                    @RequestParam String NombreEvaluacion,
                                    @RequestParam(required = false) String nivel,
                                    @RequestParam(required = false) String puntaje,
                                    @RequestParam(required = false) String puntajeMax,
                                    RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.agregarEvaluacion(codigo, NombreEvaluacion, nivel, puntaje, puntajeMax);
            redirectAttributes.addFlashAttribute("mensaje", "Evaluación registrada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias?tipo=codigo&valor=" + codigo;
    }

    // ── Visita ───────────────────────────────────────────────────────────────

    @PostMapping("/visita/guardar")
    public String guardarVisita(@RequestParam String codigo,
                                @RequestParam(required = false) String fecha,
                                @RequestParam(required = false) String hora,
                                @RequestParam(required = false) String tratamiento,
                                @RequestParam(required = false) String descripcion,
                                RedirectAttributes redirectAttributes) {
        try {
            historiaClinicaService.agregarVisita(codigo, fecha, hora, tratamiento, descripcion);
            redirectAttributes.addFlashAttribute("mensaje", "Visita registrada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/especialista/historias?tipo=codigo&valor=" + codigo;
    }

    // ── APIs externas ────────────────────────────────────────────────────────

    @GetMapping(value = "/dni/consultar", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> consultarDni(@RequestParam String numero) {
        if (!historiaClinicaService.esDniValido(numero)) {
            return ResponseEntity.badRequest().body(Map.of("error", "DNI debe tener 8 dígitos numéricos"));
        }
        try {
            DniRespuestaDto resultado = historiaClinicaService.consultarDni(numero);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping(value = "/icd/buscar", produces = "application/json")
    @ResponseBody
    public List<IcdResultadoDto> buscarIcd(@RequestParam String q) {
        return historiaClinicaService.buscarEnIcd(q);
    }
}
