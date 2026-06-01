package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.ServicioForm;
import com.springboot.proyectospring.model.Servicio;
import com.springboot.proyectospring.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listarCatalogo() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> buscarPorId(Long id) {
        return servicioRepository.findById(id);
    }

    @Transactional
    public Servicio guardar(ServicioForm form) {
        validar(form);

        Servicio servicio;
        if (form.getId() != null) {
            servicio = servicioRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));
        } else {
            servicio = new Servicio();
        }

        servicio.setNombre(form.getNombre().trim());
        servicio.setPrecioUn(form.getPrecioUn());
        servicio.setDuracion(form.getDuracion());
        return servicioRepository.save(servicio);
    }

    private void validar(ServicioForm form) {
        if (form.getNombre() == null || form.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del servicio es obligatorio");
        }
        if (form.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres");
        }
        if (form.getPrecioUn() != null && form.getPrecioUn().signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (form.getDuracion() != null && form.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }
    }
}
