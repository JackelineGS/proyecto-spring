package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.model.PaqueteSesion;
import com.springboot.proyectospring.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ServicioService servicioService;

    public HomeController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping("/")
    public String inicio() {
        return "public/index";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("serviciosGenerales", servicioService.listarGenerales());
        model.addAttribute("serviciosUnaSesion", servicioService.listarReservablesPorPaquete(PaqueteSesion.UNA));
        model.addAttribute("serviciosCuatro", servicioService.listarReservablesPorPaquete(PaqueteSesion.CUATRO));
        model.addAttribute("serviciosDiez", servicioService.listarReservablesPorPaquete(PaqueteSesion.DIEZ));
        return "public/servicios";
    }
}
