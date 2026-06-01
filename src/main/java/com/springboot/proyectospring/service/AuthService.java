package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.model.RolUsuario;
import com.springboot.proyectospring.model.SesionEmpleado;
import com.springboot.proyectospring.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    public static final String SESSION_USUARIO = "usuario";

    private final EmpleadoRepository empleadoRepository;

    public AuthService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Optional<SesionEmpleado> autenticar(String correo, String password) {
        if (correo == null || correo.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }
        return empleadoRepository.findByCorreoIgnoreCase(correo.trim())
                .filter(Empleado::isActivo)
                .filter(e -> password.equals(e.getContrasenia()))
                .map(SesionEmpleado::from);
    }

    public String rutaInicioSesion(RolUsuario rol) {
        return rol.esAdmin() ? "/admin/empleados" : "/especialista/citas";
    }
}
