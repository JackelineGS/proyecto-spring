package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.dto.PaqueteEditDto;
import com.springboot.proyectospring.dto.PaqueteForm;
import com.springboot.proyectospring.dto.ServicioForm;
import com.springboot.proyectospring.service.EmpleadoService;
import com.springboot.proyectospring.service.PaqueteService;
import com.springboot.proyectospring.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmpleadoService empleadoService;
    private final ServicioService servicioService;
    private final PaqueteService paqueteService;

    public AdminController(EmpleadoService empleadoService,
                           ServicioService servicioService,
                           PaqueteService paqueteService) {
        this.empleadoService = empleadoService;
        this.servicioService = servicioService;
        this.paqueteService = paqueteService;
    }

    @GetMapping("/empleados")
    public String empleados(Model model) {
        model.addAttribute("empleados", empleadoService.listarTodosAdmin());
        return "admin/empleados";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("activeMenu", "servicios");
        model.addAttribute("servicios", servicioService.listarCatalogo());
        return "admin/servicios";
    }

    @PostMapping("/servicios/guardar")
    public String guardarServicio(@ModelAttribute ServicioForm form, RedirectAttributes redirectAttributes) {
        try {
            boolean esNuevo = form.getId() == null;
            servicioService.guardar(form);
            redirectAttributes.addFlashAttribute("mensaje",
                    esNuevo ? "Servicio creado correctamente." : "Servicio actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/servicios";
    }

    @GetMapping("/paquetes")
    public String paquetes(Model model) {
        model.addAttribute("activeMenu", "paquetes");
        model.addAttribute("paquetes", paqueteService.listarParaAdmin());
        model.addAttribute("serviciosCatalogo", servicioService.listarCatalogo());
        return "admin/paquetes";
    }

    @GetMapping("/paquetes/{id}/editar")
    @ResponseBody
    public PaqueteEditDto paqueteParaEdicion(@PathVariable Long id) {
        return paqueteService.obtenerParaEdicion(id);
    }

    @PostMapping("/paquetes/guardar")
    public String guardarPaquete(@ModelAttribute PaqueteForm form, RedirectAttributes redirectAttributes) {
        try {
            boolean esNuevo = form.getId() == null;
            paqueteService.guardar(form);
            redirectAttributes.addFlashAttribute("mensaje",
                    esNuevo ? "Paquete creado correctamente." : "Paquete actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/paquetes";
    }
}
