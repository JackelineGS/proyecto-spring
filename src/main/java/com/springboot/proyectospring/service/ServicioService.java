package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.PaqueteSesion;
import com.springboot.proyectospring.model.Servicio;
import com.springboot.proyectospring.model.TipoServicio;
import com.springboot.proyectospring.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listarGenerales() {
        return servicioRepository.findByTipo(TipoServicio.GENERAL);
    }

    public List<Servicio> listarReservablesPorPaquete(PaqueteSesion paquete) {
        return servicioRepository.findByTipoAndPaquete(TipoServicio.RESERVABLE, paquete);
    }

    public Optional<Servicio> buscarPorId(Long id) {
        return servicioRepository.findById(id);
    }
}
