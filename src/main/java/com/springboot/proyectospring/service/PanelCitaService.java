package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.CitaPanelDto;
import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.model.Cita;
import com.springboot.proyectospring.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class PanelCitaService {

    private static final DateTimeFormatter FECHA_CORTA = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("es"));
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final CitaRepository citaRepository;
    private final HistoriaClinicaService historiaClinicaService;

    public PanelCitaService(CitaRepository citaRepository, HistoriaClinicaService historiaClinicaService) {
        this.citaRepository = citaRepository;
        this.historiaClinicaService = historiaClinicaService;
    }

    public List<String> diasSemana() {
        return List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");
    }

    public List<CitaPanelDto> listarCitas(Long empleadoId, int semanaOffset) {
        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.with(DayOfWeek.MONDAY).plusWeeks(semanaOffset);
        LocalDate sabado = lunes.plusDays(5);
        return citaRepository.findByEmpleadoAndRangoFechas(empleadoId, lunes, sabado).stream()
                .map(this::toDto)
                .toList();
    }

    public List<List<CitaPanelDto>> citasPorDia(Long empleadoId, int semanaOffset) {
        List<CitaPanelDto> citas = listarCitas(empleadoId, semanaOffset);
        List<List<CitaPanelDto>> porDia = new ArrayList<>();
        for (int d = 0; d < 6; d++) {
            final int dia = d;
            porDia.add(citas.stream().filter(c -> c.getDia() == dia).toList());
        }
        return porDia;
    }

    private CitaPanelDto toDto(Cita cita) {
        CitaPanelDto dto = new CitaPanelDto();
        if (cita.getFecha() != null) {
            dto.setDia(cita.getFecha().getDayOfWeek().getValue() - 1);
            dto.setFecha(FECHA_CORTA.format(cita.getFecha()));
        }
        if (cita.getHora() != null) {
            dto.setHora(cita.getHora().format(HORA_FMT));
        }
        dto.setPaciente(cita.getPaciente().getNombreCompleto());
        dto.setNumDocumento(cita.getPaciente().getNumDocumento());
        dto.setPaqueteNombre(cita.getPaquete() != null ? cita.getPaquete().getNombre() : "—");
        dto.setEstado(cita.getEstado());
        historiaClinicaService.resumenPorPacienteId(cita.getPaciente().getId())
                .ifPresent(dto::setHistoria);
        if (dto.getHistoria() == null) {
            HistoriaClinicaDetalle min = new HistoriaClinicaDetalle();
            min.setNumHc("—");
            min.setNumDocumento(cita.getPaciente().getNumDocumento());
            dto.setHistoria(min);
        }
        return dto;
    }
}
