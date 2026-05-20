package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.service.EspecialistaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EspecialistaController {

    private final EspecialistaService especialistaService;

    public EspecialistaController(EspecialistaService especialistaService) {
        this.especialistaService = especialistaService;
    }

    @GetMapping({"/profesionales", "/especialistas"})
    public String profesionales(Model model) {
        model.addAttribute("especialistas", especialistaService.listarTodos());
        return "public/especialistas";
    }
}
