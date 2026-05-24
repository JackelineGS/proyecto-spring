package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.Psicofarmaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsicofarmacoRepository extends JpaRepository<Psicofarmaco, Long> {
}
