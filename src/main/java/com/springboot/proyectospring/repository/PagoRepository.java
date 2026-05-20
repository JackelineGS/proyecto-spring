package com.springboot.proyectospring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.proyectospring.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}

