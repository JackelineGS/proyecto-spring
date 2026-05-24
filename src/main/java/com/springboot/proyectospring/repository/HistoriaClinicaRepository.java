package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    Optional<HistoriaClinica> findByNumHcIgnoreCase(String numHc);

    @Query("""
            SELECT h FROM HistoriaClinica h
            JOIN FETCH h.paciente p
            WHERE p.numDocumento = :numDocumento
            """)
    Optional<HistoriaClinica> findByPacienteNumDocumento(String numDocumento);

    @Query("""
            SELECT h FROM HistoriaClinica h
            JOIN FETCH h.paciente
            WHERE UPPER(h.numHc) = UPPER(:numHc)
            """)
    Optional<HistoriaClinica> findDetalleByNumHc(String numHc);

    @Query("""
            SELECT h FROM HistoriaClinica h
            JOIN FETCH h.paciente
            WHERE h.paciente.numDocumento = :numDocumento
            """)
    Optional<HistoriaClinica> findDetalleByPacienteNumDocumento(String numDocumento);

    Optional<HistoriaClinica> findByPaciente_Id(Long pacienteId);
}
