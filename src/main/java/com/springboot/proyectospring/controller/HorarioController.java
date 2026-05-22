package com.springboot.proyectospring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @GetMapping("/{especialistaId}")
    public List<String> horariosPorEspecialista(@PathVariable Long especialistaId) {
        // Generación dinámica de horarios de 7:00 am a 6:00 pm (18 horas)
        // Esto evita llenar la base de datos hasta que se implemente una agenda real.
        List<String> horarios = new ArrayList<>();
        for (int i = 7; i <= 18; i++) {
            String sufijo = (i < 12) ? "am" : "pm";
            int horaFormat = (i > 12) ? i - 12 : i;
            // Se formatea como "7:00 am", "12:00 pm", "1:00 pm", etc.
            horarios.add(horaFormat + ":00 " + sufijo);
        }
        return horarios;
    }
}
