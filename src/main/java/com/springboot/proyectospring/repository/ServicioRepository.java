package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
