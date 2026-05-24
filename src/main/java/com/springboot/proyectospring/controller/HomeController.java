package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.service.EmpleadoService;
import com.springboot.proyectospring.service.PaqueteService;
import com.springboot.proyectospring.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ServicioService servicioService;
    private final PaqueteService paqueteService;
    private final EmpleadoService empleadoService;

    public HomeController(ServicioService servicioService,
                          PaqueteService paqueteService,
                          EmpleadoService empleadoService) {
        this.servicioService = servicioService;
        this.paqueteService = paqueteService;
        this.empleadoService = empleadoService;
    }

    @GetMapping("/")
    public String inicio() {
        return "public/index";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("serviciosCatalogo", servicioService.listarCatalogo());
        model.addAttribute("paquetesUnaSesion", paqueteService.listarPorTotalSesiones(1));
        model.addAttribute("paquetesCuatro", paqueteService.listarPorTotalSesiones(4));
        model.addAttribute("paquetesDiez", paqueteService.listarPorTotalSesiones(10));
        return "public/servicios";
    }

    @GetMapping("/especialistas")
    public String especialistas(Model model) {
        model.addAttribute("empleados", empleadoService.listarEspecialistasActivos());
        return "public/especialistas";
    }
}
