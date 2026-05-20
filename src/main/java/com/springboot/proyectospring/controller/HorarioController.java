package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.service.HorarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping("/{especialistaId}")
    public List<String> horariosPorEspecialista(@PathVariable Long especialistaId) {
        return horarioService.listarHorasPorEspecialista(especialistaId);
    }
}
