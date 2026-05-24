package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.repository.HistoriaClinicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class HistoriaClinicaService {

    private static final Pattern DNI_REGEX = Pattern.compile("^\\d{8}$");
    private static final Pattern HC_REGEX = Pattern.compile("^HC-\\d{4}-\\d{5}$", Pattern.CASE_INSENSITIVE);

    private final HistoriaClinicaRepository historiaClinicaRepository;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository) {
        this.historiaClinicaRepository = historiaClinicaRepository;
    }

    public Optional<HistoriaClinicaDetalle> buscarPorDni(String dni) {
        if (!DNI_REGEX.matcher(dni).matches()) {
            return Optional.empty();
        }
        return historiaClinicaRepository.findDetalleByPacienteNumDocumento(dni)
                .map(HistoriaClinicaMapper::toDetalle);
    }

    public Optional<HistoriaClinicaDetalle> buscarPorCodigo(String codigo) {
        if (!HC_REGEX.matcher(codigo).matches()) {
            return Optional.empty();
        }
        return historiaClinicaRepository.findDetalleByNumHc(codigo.toUpperCase())
                .map(HistoriaClinicaMapper::toDetalle);
    }

    public boolean esDniValido(String dni) {
        return DNI_REGEX.matcher(dni).matches();
    }

    public boolean esCodigoValido(String codigo) {
        return HC_REGEX.matcher(codigo).matches();
    }

    public Optional<HistoriaClinicaDetalle> resumenPorPacienteId(Long pacienteId) {
        return historiaClinicaRepository.findByPaciente_Id(pacienteId)
                .map(HistoriaClinicaMapper::toDetalle);
    }
}
