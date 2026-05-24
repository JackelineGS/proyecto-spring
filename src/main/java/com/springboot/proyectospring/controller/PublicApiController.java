package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.DniRespuestaDto;
import com.springboot.proyectospring.service.DniApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class PublicApiController {

    private static final Pattern DNI_REGEX = Pattern.compile("^\\d{8}$");

    private final DniApiClient dniApiClient;

    public PublicApiController(DniApiClient dniApiClient) {
        this.dniApiClient = dniApiClient;
    }

    @GetMapping("/dni")
    public ResponseEntity<?> consultarDni(@RequestParam String numero) {
        if (!DNI_REGEX.matcher(numero).matches()) {
            return ResponseEntity.badRequest().body(Map.of("error", "DNI debe tener 8 dígitos numéricos"));
        }
        try {
            DniRespuestaDto resultado = dniApiClient.consultar(numero);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
