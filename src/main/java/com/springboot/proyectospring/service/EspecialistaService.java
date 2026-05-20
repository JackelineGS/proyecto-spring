package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Especialista;
import com.springboot.proyectospring.repository.EspecialistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialistaService {

    private final EspecialistaRepository especialistaRepository;

    public EspecialistaService(EspecialistaRepository especialistaRepository) {
        this.especialistaRepository = especialistaRepository;
    }

    public List<Especialista> listarTodos() {
        return especialistaRepository.findAll();
    }

    public Optional<Especialista> buscarPorId(Long id) {
        return especialistaRepository.findById(id);
    }
}
