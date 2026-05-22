package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.TipoPaquete;
import com.springboot.proyectospring.repository.TipoPaqueteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoPaqueteService {
    private final TipoPaqueteRepository repository;

    public TipoPaqueteService(TipoPaqueteRepository repository) {
        this.repository = repository;
    }

    public List<TipoPaquete> listarTodos() {
        return repository.findAll();
    }
}