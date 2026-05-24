package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
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
}
