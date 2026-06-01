package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.DniRespuestaDto;
import com.springboot.proyectospring.service.DniApiClient;
import com.springboot.proyectospring.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class PublicApiController {

    private static final Pattern DNI_REGEX = Pattern.compile("^\\d{8}$");

    private final DniApiClient dniApiClient;
    private final CitaService citaService;

    public PublicApiController(DniApiClient dniApiClient, CitaService citaService) {
        this.dniApiClient = dniApiClient;
        this.citaService = citaService;
    }

    @GetMapping("/dni")
    public ResponseEntity<?> consultarDni(@RequestParam String numero, HttpSession session) {
        if (!DNI_REGEX.matcher(numero).matches()) {
            return ResponseEntity.badRequest().body(Map.of("error", "DNI debe tener 8 dígitos numéricos"));
        }
        try {
            DniRespuestaDto resultado = dniApiClient.consultar(numero);
            session.setAttribute("dniData", resultado);
            session.setAttribute("dniNumero", numero);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/horarios-ocupados/{empleadoId}")
    public ResponseEntity<List<String>> getHorariosOcupados(@PathVariable Long empleadoId) {
        List<String> ocupados = citaService.obtenerHorariosOcupados(empleadoId);
        return ResponseEntity.ok(ocupados);
    }
}
