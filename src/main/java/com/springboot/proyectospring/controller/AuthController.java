package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.model.SesionEmpleado;
import com.springboot.proyectospring.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String correo,
                              @RequestParam String password,
                              HttpServletRequest request,
                              Model model) {
        Optional<SesionEmpleado> userOpt = authService.autenticar(correo, password);

        if (userOpt.isPresent()) {
            SesionEmpleado usuario = userOpt.get();
            HttpSession session = request.getSession(true);
            session.setAttribute(AuthService.SESSION_USUARIO, usuario);
            return "redirect:" + authService.rutaInicioSesion(usuario.getRol());
        }
        model.addAttribute("error", "Credenciales incorrectas");
        return "auth/login";
    }
}
