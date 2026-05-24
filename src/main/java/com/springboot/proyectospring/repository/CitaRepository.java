package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("""
            SELECT c FROM Cita c
            JOIN FETCH c.paciente
            JOIN FETCH c.paquete
            WHERE c.empleado.id = :empleadoId
            AND c.fecha BETWEEN :desde AND :hasta
            ORDER BY c.fecha, c.hora
            """)
    List<Cita> findByEmpleadoAndRangoFechas(Long empleadoId, LocalDate desde, LocalDate hasta);
}
