package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.DetallePaquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetallePaqueteRepository extends JpaRepository<DetallePaquete, Long> {

    @Query("""
            SELECT d FROM DetallePaquete d
            JOIN FETCH d.servicio
            WHERE d.paquete.id = :paqueteId
            """)
    List<DetallePaquete> findByPaqueteIdWithServicio(Long paqueteId);

    @Modifying
    @Transactional
    void deleteByPaquete_Id(Long paqueteId);
}
