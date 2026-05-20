package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Horario;
import com.springboot.proyectospring.repository.HorarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    public List<String> listarHorasPorEspecialista(Long especialistaId) {
        return horarioRepository.findByEspecialistaId(especialistaId).stream()
                .map(Horario::getHora)
                .toList();
    }
}
