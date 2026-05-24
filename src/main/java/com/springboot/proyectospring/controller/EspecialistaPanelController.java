package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.model.SesionEmpleado;
import com.springboot.proyectospring.service.AuthService;
import com.springboot.proyectospring.service.HistoriaClinicaService;
import com.springboot.proyectospring.service.HorarioService;
import com.springboot.proyectospring.service.PanelCitaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/especialista")
public class EspecialistaPanelController {

    private static final List<String> SLOTS_POSIBLES = List.of(
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00", "20:00", "21:00");

    private final PanelCitaService panelCitaService;
    private final HistoriaClinicaService historiaClinicaService;
    private final HorarioService horarioService;

    public EspecialistaPanelController(PanelCitaService panelCitaService,
                                       HistoriaClinicaService historiaClinicaService,
                                       HorarioService horarioService) {
        this.panelCitaService = panelCitaService;
        this.historiaClinicaService = historiaClinicaService;
        this.horarioService = horarioService;
    }

    @GetMapping("/citas")
    public String citas(HttpSession session, Model model) {
        Long empleadoId = empleadoIdSesion(session);
        model.addAttribute("active", "citas");
        model.addAttribute("dias", panelCitaService.diasSemana());
        model.addAttribute("citasPorDia", panelCitaService.citasPorDia(empleadoId));
        model.addAttribute("citasData", panelCitaService.listarCitas(empleadoId));
        return "especialista/citas";
    }

    @GetMapping("/historias")
    public String historias(@RequestParam(required = false) String tipo,
                            @RequestParam(required = false) String valor,
                            Model model) {
        model.addAttribute("active", "historias");
        model.addAttribute("tipo", tipo != null ? tipo : "dni");
        model.addAttribute("valor", valor != null ? valor : "");
        model.addAttribute("busquedaRealizada", false);
        model.addAttribute("resultado", null);

        if (valor != null && !valor.isBlank()) {
            Optional<HistoriaClinicaDetalle> resultado;
            String error = null;

            if ("codigo".equals(tipo)) {
                if (!historiaClinicaService.esCodigoValido(valor.trim())) {
                    error = "El código debe tener el formato HC-AAAA-00000.";
                    resultado = Optional.empty();
                } else {
                    resultado = historiaClinicaService.buscarPorCodigo(valor.trim());
                }
            } else {
                if (!historiaClinicaService.esDniValido(valor.trim())) {
                    error = "El DNI debe tener 8 dígitos numéricos.";
                    resultado = Optional.empty();
                } else {
                    resultado = historiaClinicaService.buscarPorDni(valor.trim());
                }
            }

            model.addAttribute("error", error);
            model.addAttribute("resultado", resultado.orElse(null));
            model.addAttribute("busquedaRealizada", error == null);
        }

        return "especialista/historias";
    }

    @GetMapping("/horarios")
    public String horarios(HttpSession session, Model model) {
        Long empleadoId = empleadoIdSesion(session);
        model.addAttribute("active", "horarios");
        model.addAttribute("dias", List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"));
        model.addAttribute("slots", SLOTS_POSIBLES);
        model.addAttribute("horariosIniciales", horarioService.horariosPorDia(empleadoId));
        return "especialista/horarios";
    }

    private Long empleadoIdSesion(HttpSession session) {
        Object attr = session.getAttribute(AuthService.SESSION_USUARIO);
        if (attr instanceof SesionEmpleado sesion) {
            return sesion.getId();
        }
        throw new IllegalStateException("Sesión de especialista no válida");
    }
}
