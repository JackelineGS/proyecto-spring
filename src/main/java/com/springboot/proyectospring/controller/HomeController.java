package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.service.TipoPaqueteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final TipoPaqueteService tipoPaqueteService;

    public HomeController(TipoPaqueteService tipoPaqueteService) {
        this.tipoPaqueteService = tipoPaqueteService;
    }

    @GetMapping("/")
    public String inicio() {
        return "public/index";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("tiposPaquete", tipoPaqueteService.listarTodos());
        return "public/servicios";
    }
}
