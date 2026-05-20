package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Horario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HorarioRepository {

    private final List<Horario> horarios = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    public Horario save(Horario horario) {
        if (horario.getId() == null) {
            horario.setId(secuencia.getAndIncrement());
        }
        horarios.removeIf(h -> h.getId().equals(horario.getId()));
        horarios.add(horario);
        return horario;
    }

    public List<Horario> findByEspecialistaId(Long especialistaId) {
        return horarios.stream()
                .filter(h -> h.getEspecialistaId().equals(especialistaId))
                .toList();
    }

    public List<Horario> findAll() {
        return List.copyOf(horarios);
    }
}
