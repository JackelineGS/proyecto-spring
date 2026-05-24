package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("""
            SELECT p FROM Pago p
            JOIN FETCH p.paciente
            JOIN FETCH p.paquete
            WHERE p.id = :id
            """)
    Optional<Pago> findConDetalles(@Param("id") Long id);
}
