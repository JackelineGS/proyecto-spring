package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
}