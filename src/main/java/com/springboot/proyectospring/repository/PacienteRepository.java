package com.springboot.proyectospring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.proyectospring.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}

