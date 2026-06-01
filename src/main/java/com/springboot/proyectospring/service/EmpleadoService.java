package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final com.springboot.proyectospring.repository.EspecialidadRepository especialidadRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           com.springboot.proyectospring.repository.EspecialidadRepository especialidadRepository) {
        this.empleadoRepository = empleadoRepository;
        this.especialidadRepository = especialidadRepository;
    }

    public List<Empleado> listarTodosAdmin() {
        return empleadoRepository.findAllWithEspecialidad();
    }

    public List<Empleado> listarEspecialistasActivos() {
        return empleadoRepository.findEspecialistasActivos();
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepository.findById(id);
    }

    public void guardar(com.springboot.proyectospring.dto.EmpleadoForm form) {
        Empleado empleado;
        if (form.getId() != null) {
            empleado = empleadoRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        } else {
            empleado = new Empleado();
        }

        empleado.setNombre(form.getNombre());
        empleado.setApellido(form.getApellido() != null ? form.getApellido() : "");
        empleado.setCorreo(form.getCorreo());
        empleado.setCelular(form.getCelular());
        empleado.setRol(form.getRol());
        empleado.setEstado(form.getEstado() != null ? form.getEstado() : false);

        boolean esNuevo = form.getId() == null;
        if (esNuevo) {
            if (form.getContrasenia() == null || form.getContrasenia().isBlank())
                throw new IllegalArgumentException("La contraseña es obligatoria al crear un empleado.");
            empleado.setContrasenia(form.getContrasenia().trim());
        } else if (form.getContrasenia() != null && !form.getContrasenia().isBlank()) {
            empleado.setContrasenia(form.getContrasenia().trim());
        }

        if (form.getEspecialidadId() != null) {
            empleado.setEspecialidad(especialidadRepository.findById(form.getEspecialidadId()).orElse(null));
        } else {
            empleado.setEspecialidad(null);
        }

        empleadoRepository.save(empleado);
    }
}
