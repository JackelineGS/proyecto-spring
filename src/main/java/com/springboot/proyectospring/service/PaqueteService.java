package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.PaqueteAdminDto;
import com.springboot.proyectospring.dto.PaqueteDetalleItem;
import com.springboot.proyectospring.dto.PaqueteEditDto;
import com.springboot.proyectospring.dto.PaqueteForm;
import com.springboot.proyectospring.model.DetallePaquete;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.model.Servicio;
import com.springboot.proyectospring.repository.DetallePaqueteRepository;
import com.springboot.proyectospring.repository.PaqueteRepository;
import com.springboot.proyectospring.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final DetallePaqueteRepository detallePaqueteRepository;
    private final ServicioRepository servicioRepository;

    public PaqueteService(PaqueteRepository paqueteRepository,
                          DetallePaqueteRepository detallePaqueteRepository,
                          ServicioRepository servicioRepository) {
        this.paqueteRepository = paqueteRepository;
        this.detallePaqueteRepository = detallePaqueteRepository;
        this.servicioRepository = servicioRepository;
    }

    public Optional<Paquete> buscarPorId(Long id) {
        return paqueteRepository.findById(id);
    }

    public List<Paquete> listarPorTotalSesiones(int sesiones) {
        return paqueteRepository.findByTotalSesiones(sesiones);
    }

    public List<PaqueteAdminDto> listarParaAdmin() {
        List<PaqueteAdminDto> resultado = new ArrayList<>();
        for (Paquete paquete : paqueteRepository.findAll()) {
            PaqueteAdminDto dto = new PaqueteAdminDto();
            dto.setId(paquete.getId());
            dto.setNombre(paquete.getNombre());
            dto.setDescripcion(paquete.getDescripcion());
            dto.setPrecioTotal(paquete.getPrecioTotal());
            dto.setTotalSesiones(paquete.getTotalSesiones());
            detallePaqueteRepository.findByPaqueteIdWithServicio(paquete.getId()).forEach(d -> {
                String etiqueta = d.getServicio().getNombre();
                if (d.getCantidad() != null && d.getCantidad() > 1) {
                    etiqueta += " x" + d.getCantidad();
                }
                dto.getServiciosIncluidos().add(etiqueta);
            });
            resultado.add(dto);
        }
        return resultado;
    }

    public PaqueteEditDto obtenerParaEdicion(Long id) {
        Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado"));

        PaqueteEditDto dto = new PaqueteEditDto();
        dto.setId(paquete.getId());
        dto.setNombre(paquete.getNombre());
        dto.setDescripcion(paquete.getDescripcion());
        dto.setPrecioTotal(paquete.getPrecioTotal());
        dto.setTotalSesiones(paquete.getTotalSesiones());

        List<PaqueteDetalleItem> detalles = detallePaqueteRepository.findByPaqueteIdWithServicio(id).stream()
                .map(d -> new PaqueteDetalleItem(
                        d.getServicio().getId(),
                        d.getServicio().getNombre(),
                        d.getCantidad() != null ? d.getCantidad() : 1))
                .toList();
        dto.setDetalles(detalles);
        return dto;
    }

    @Transactional
    public Paquete guardar(PaqueteForm form) {
        validar(form);

        Paquete paquete;
        if (form.getId() != null) {
            paquete = paqueteRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Paquete no encontrado"));
            detallePaqueteRepository.deleteByPaquete_Id(paquete.getId());
        } else {
            paquete = new Paquete();
        }

        paquete.setNombre(form.getNombre().trim());
        paquete.setDescripcion(form.getDescripcion() != null ? form.getDescripcion().trim() : null);
        paquete.setPrecioTotal(form.getPrecioTotal());
        paquete.setTotalSesiones(form.getTotalSesiones());
        paquete = paqueteRepository.save(paquete);

        guardarDetalles(paquete, form.getServicioIds(), form.getCantidades());
        return paquete;
    }

    private void guardarDetalles(Paquete paquete, List<Long> servicioIds, List<Integer> cantidades) {
        for (int i = 0; i < servicioIds.size(); i++) {
            Long servicioId = servicioIds.get(i);
            if (servicioId == null) {
                continue;
            }
            Servicio servicio = servicioRepository.findById(servicioId)
                    .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado: " + servicioId));

            int cantidad = 1;
            if (i < cantidades.size() && cantidades.get(i) != null && cantidades.get(i) > 0) {
                cantidad = cantidades.get(i);
            }

            DetallePaquete detalle = new DetallePaquete();
            detalle.setPaquete(paquete);
            detalle.setServicio(servicio);
            detalle.setCantidad(cantidad);
            detallePaqueteRepository.save(detalle);
        }
    }

    private void validar(PaqueteForm form) {
        if (form.getNombre() == null || form.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del paquete es obligatorio");
        }
        if (form.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres");
        }
        if (form.getPrecioTotal() != null && form.getPrecioTotal().signum() < 0) {
            throw new IllegalArgumentException("El precio total no puede ser negativo");
        }
        if (form.getTotalSesiones() != null && form.getTotalSesiones() <= 0) {
            throw new IllegalArgumentException("El total de sesiones debe ser mayor a 0");
        }
        if (form.getServicioIds() == null || form.getServicioIds().isEmpty()) {
            throw new IllegalArgumentException("Debes agregar al menos un servicio al paquete");
        }
    }
}
