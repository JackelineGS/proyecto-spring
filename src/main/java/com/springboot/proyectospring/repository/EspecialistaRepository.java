package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Especialista;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EspecialistaRepository {

    private final List<Especialista> especialistas = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    public Especialista save(Especialista especialista) {
        if (especialista.getId() == null) {
            especialista.setId(secuencia.getAndIncrement());
        }
        especialistas.removeIf(e -> e.getId().equals(especialista.getId()));
        especialistas.add(especialista);
        return especialista;
    }

    public Optional<Especialista> findById(Long id) {
        return especialistas.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public List<Especialista> findAll() {
        return List.copyOf(especialistas);
    }
}
