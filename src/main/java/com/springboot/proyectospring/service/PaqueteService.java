package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.repository.PaqueteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaqueteService {
    private final PaqueteRepository repository;

    public PaqueteService(PaqueteRepository repository) {
        this.repository = repository;
    }

    public Optional<Paquete> buscarPorId(Long id) {
        return repository.findById(id);
    }
}