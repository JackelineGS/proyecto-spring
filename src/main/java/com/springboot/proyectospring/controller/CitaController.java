package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.ReservaRequest;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.service.CitaService;
import com.springboot.proyectospring.service.EspecialistaService;
import com.springboot.proyectospring.service.PaqueteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class CitaController {

    private final PaqueteService paqueteService;
    private final EspecialistaService especialistaService;
    private final CitaService citaService;

    public CitaController(PaqueteService paqueteService,
                          EspecialistaService especialistaService,
                          CitaService citaService) {
        this.paqueteService = paqueteService;
        this.especialistaService = especialistaService;
        this.citaService = citaService;
    }

    @GetMapping("/reservar/{id}")
    public String reservar(@PathVariable Long id, Model model) {
        Paquete paquete = paqueteService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado"));
        model.addAttribute("paquete", paquete);
        model.addAttribute("especialistas", especialistaService.listarTodos());
        return "public/reservar";
    }

    @PostMapping("/reservar/confirmar")
    public String confirmar(@ModelAttribute ReservaRequest request, RedirectAttributes redirectAttributes) {
        Map<String, Object> confirmacion = citaService.confirmarReserva(request);
        redirectAttributes.addFlashAttribute("confirmacion", confirmacion);
        return "redirect:/reservar/exito";
    }

    @GetMapping("/reservar/exito")
    public String exito() {
        return "public/reserva-exito";
    }
}
