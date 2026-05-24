package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.service.HistoriaClinicaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/especialista/historias")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    public HistoriaClinicaController(HistoriaClinicaService historiaClinicaService) {
        this.historiaClinicaService = historiaClinicaService;
    }

    @GetMapping("/detalle/{codigo}")
    public String detalle(@PathVariable String codigo, Model model) {
        HistoriaClinicaDetalle historia = historiaClinicaService.buscarPorCodigo(codigo)
                .orElse(null);
        model.addAttribute("historia", historia);
        return "especialista/fragments/historia-modal :: modal";
    }
}
