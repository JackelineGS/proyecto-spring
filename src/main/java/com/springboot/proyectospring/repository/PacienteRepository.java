package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByNumDocumento(String numDocumento);
}
