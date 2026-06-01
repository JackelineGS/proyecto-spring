package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaqueteRepository extends JpaRepository<Paquete, Long> {

    List<Paquete> findByTotalSesiones(Integer totalSesiones);
}
