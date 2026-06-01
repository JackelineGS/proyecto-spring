package com.springboot.proyectospring.config;

import com.springboot.proyectospring.model.RolUsuario;
import com.springboot.proyectospring.model.SesionEmpleado;
import com.springboot.proyectospring.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();

        if (path.startsWith("/especialista")) {
            return verificarRol(request, response, RolUsuario.ESPECIALISTA);
        }
        if (path.startsWith("/admin")) {
            return verificarRol(request, response, RolUsuario.ADMIN);
        }
        return true;
    }

    private boolean verificarRol(HttpServletRequest request, HttpServletResponse response, RolUsuario rolRequerido)
            throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/login");
            return false;
        }
        Object attr = session.getAttribute(AuthService.SESSION_USUARIO);
        if (!(attr instanceof SesionEmpleado usuario)) {
            response.sendRedirect("/login");
            return false;
        }
        if (usuario.getRol() != rolRequerido) {
            response.sendRedirect(authServiceRedirect(usuario));
            return false;
        }
        return true;
    }

    private String authServiceRedirect(SesionEmpleado usuario) {
        return usuario.getRol().esAdmin() ? "/admin/empleados" : "/especialista/citas";
    }
}
