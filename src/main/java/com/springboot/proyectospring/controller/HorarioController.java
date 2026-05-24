package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.model.SesionEmpleado;
import com.springboot.proyectospring.service.AuthService;
import com.springboot.proyectospring.service.HorarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping("/{empleadoId}")
    public List<String> horariosPorEmpleado(@PathVariable Long empleadoId) {
        return horarioService.listarHorasPorEmpleado(empleadoId);
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarHorarios(@RequestBody Map<String, List<String>> horariosSeleccionados,
                                             HttpSession session) {
        try {
            Long empleadoId = empleadoIdSesion(session);
            horarioService.guardarHorarios(empleadoId, horariosSeleccionados);
            return ResponseEntity.ok(Map.of("mensaje", "Horarios actualizados con éxito"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al guardar: " + e.getMessage()));
        }
    }

    private Long empleadoIdSesion(HttpSession session) {
        Object attr = session.getAttribute(AuthService.SESSION_USUARIO);
        if (attr instanceof SesionEmpleado sesion) {
            return sesion.getId();
        }
        throw new IllegalStateException("Sesión no válida");
    }
}
