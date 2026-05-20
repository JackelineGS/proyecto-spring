package com.springboot.proyectospring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.proyectospring.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
}

