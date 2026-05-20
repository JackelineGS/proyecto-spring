package com.springboot.proyectospring.repository;

import com.springboot.proyectospring.model.PaqueteSesion;
import com.springboot.proyectospring.model.Servicio;
import com.springboot.proyectospring.model.TipoServicio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ServicioRepository {

    private final List<Servicio> servicios = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    public Servicio save(Servicio servicio) {
        if (servicio.getId() == null) {
            servicio.setId(secuencia.getAndIncrement());
        }
        servicios.removeIf(s -> s.getId().equals(servicio.getId()));
        servicios.add(servicio);
        return servicio;
    }

    public Optional<Servicio> findById(Long id) {
        return servicios.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public List<Servicio> findAll() {
        return List.copyOf(servicios);
    }

    public List<Servicio> findByTipo(TipoServicio tipo) {
        return servicios.stream().filter(s -> s.getTipo() == tipo).toList();
    }

    public List<Servicio> findByTipoAndPaquete(TipoServicio tipo, PaqueteSesion paquete) {
        return servicios.stream()
                .filter(s -> s.getTipo() == tipo && s.getPaquete() == paquete)
                .toList();
    }
}
