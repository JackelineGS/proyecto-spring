package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByCorreoIgnoreCase(String correo);

    @Query("""
            SELECT e FROM Empleado e
            LEFT JOIN FETCH e.especialidad
            WHERE e.estado = true
            ORDER BY e.apellido, e.nombre
            """)
    List<Empleado> findEspecialistasActivos();

    @Query("""
            SELECT e FROM Empleado e
            LEFT JOIN FETCH e.especialidad
            ORDER BY e.apellido, e.nombre
            """)
    List<Empleado> findAllWithEspecialidad();
}
