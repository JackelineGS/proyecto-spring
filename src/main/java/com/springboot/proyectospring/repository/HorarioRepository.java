package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByEmpleadoIdOrderByDiaSemanaAscHoraAsc(Long empleadoId);

    @Modifying
    @Transactional
    void deleteByEmpleadoId(Long empleadoId);
}
