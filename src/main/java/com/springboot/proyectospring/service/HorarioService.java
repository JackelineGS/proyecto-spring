package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Empleado;
import com.springboot.proyectospring.model.Horario;
import com.springboot.proyectospring.repository.EmpleadoRepository;
import com.springboot.proyectospring.repository.HorarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    private static final List<String> DIAS = List.of(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final HorarioRepository horarioRepository;
    private final EmpleadoRepository empleadoRepository;

    public HorarioService(HorarioRepository horarioRepository, EmpleadoRepository empleadoRepository) {
        this.horarioRepository = horarioRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public List<String> listarHorasPorEmpleado(Long empleadoId) {
        return horarioRepository.findByEmpleadoIdOrderByDiaSemanaAscHoraAsc(empleadoId).stream()
                .map(Horario::getHora)
                .map(h -> h.format(HORA_FMT))
                .distinct()
                .toList();
    }

    public Map<String, List<String>> horariosPorDia(Long empleadoId) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String dia : DIAS) {
            map.put(dia, new ArrayList<>());
        }
        horarioRepository.findByEmpleadoIdOrderByDiaSemanaAscHoraAsc(empleadoId).forEach(h -> {
            String nombreDia = diaNombre(h.getDiaSemana());
            if (nombreDia != null) {
                map.get(nombreDia).add(h.getHora().format(HORA_FMT));
            }
        });
        return map;
    }

    @Transactional
    public void guardarHorarios(Long empleadoId, Map<String, List<String>> horariosSeleccionados) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        horarioRepository.deleteByEmpleadoId(empleadoId);

        for (Map.Entry<String, List<String>> entry : horariosSeleccionados.entrySet()) {
            Integer diaSemana = diaNumero(entry.getKey());
            if (diaSemana == null || entry.getValue() == null) {
                continue;
            }
            for (String horaTexto : entry.getValue()) {
                Horario horario = new Horario();
                horario.setEmpleado(empleado);
                horario.setDiaSemana(diaSemana);
                horario.setHora(LocalTime.parse(normalizarHora(horaTexto)));
                horarioRepository.save(horario);
            }
        }
    }

    private static String normalizarHora(String hora) {
        String h = hora.trim().toLowerCase();
        if (h.contains("am") || h.contains("pm")) {
            h = h.replace(" am", "").replace(" pm", "").replace("am", "").replace("pm", "").trim();
        }
        if (h.length() == 4 && h.charAt(1) == ':') {
            h = "0" + h;
        }
        return h.length() == 5 ? h : h + ":00";
    }

    private static Integer diaNumero(String dia) {
        for (int i = 0; i < DIAS.size(); i++) {
            if (DIAS.get(i).equalsIgnoreCase(dia)) {
                return i + 1;
            }
        }
        return null;
    }

    private static String diaNombre(Integer diaSemana) {
        if (diaSemana == null || diaSemana < 1 || diaSemana > DIAS.size()) {
            return null;
        }
        return DIAS.get(diaSemana - 1);
    }
}
