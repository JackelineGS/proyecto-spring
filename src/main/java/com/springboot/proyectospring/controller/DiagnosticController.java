package com.springboot.proyectospring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Punto débil introducido a propósito para el trabajo académico de hardening
 * (CLAUDE.md, sección 3). Ejecuta sin validación el comando recibido por
 * query param, usando un shell real para respetar metacaracteres (; && > |).
 *
 * ADVERTENCIA: esto es una inyección de comandos deliberada. Solo debe
 * existir en el entorno controlado de la demo (localhost, VMs del equipo).
 * No exponer a una red real ni incluir en una versión de producción.
 */
@RestController
public class DiagnosticController {

    @GetMapping(value = "/api/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test(@RequestParam String cmd) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", cmd);
            // Combina stdout y stderr en un solo flujo para que, por ejemplo,
            // "Permiso denegado" viaje también en la respuesta HTTP.
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            output.append("Exit code: ").append(exitCode);
        } catch (Exception e) {
            output.append("Error ejecutando el comando: ").append(e);
        }

        return output.toString();
    }
}
