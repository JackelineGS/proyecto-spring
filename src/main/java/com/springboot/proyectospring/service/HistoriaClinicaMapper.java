package com.springboot.proyectospring.service;

import com.springboot.proyectospring.dto.HistoriaClinicaDetalle;
import com.springboot.proyectospring.model.Evaluacion;
import com.springboot.proyectospring.model.HistoriaClinica;
import com.springboot.proyectospring.model.Psicofarmaco;
import com.springboot.proyectospring.model.Visita;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class HistoriaClinicaMapper {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale.forLanguageTag("es"));

    private HistoriaClinicaMapper() {
    }

    public static HistoriaClinicaDetalle toDetalle(HistoriaClinica h) {
        HistoriaClinicaDetalle dto = new HistoriaClinicaDetalle();
        dto.setNumHc(h.getNumHc());
        dto.setPaciente(h.getPaciente().getNombreCompleto());
        dto.setNumDocumento(h.getPaciente().getNumDocumento());
        if (h.getPaciente().getFechaNacimiento() != null) {
            dto.setFechaNacimiento(FECHA.format(h.getPaciente().getFechaNacimiento()));
            dto.setFechaNacimientoIso(h.getPaciente().getFechaNacimiento().toString());
            dto.setEdad(Period.between(h.getPaciente().getFechaNacimiento(), LocalDate.now()).getYears());
        }
        dto.setSexo(h.getPaciente().getSexo());
        dto.setGradoInstruccion(h.getPaciente().getGradoInstruccion());
        dto.setOcupacion(h.getPaciente().getOcupacion());
        dto.setNumeroHijos(h.getPaciente().getNumHijos() != null ? h.getPaciente().getNumHijos() : 0);
        dto.setResidencia(h.getPaciente().getResidencia());
        dto.setCelular(h.getPaciente().getCelular());
        dto.setCorreo(h.getPaciente().getCorreo());
        dto.setMotivoConsulta(h.getMotivoConsulta());
        dto.setHistNinez(h.getHistNinez());
        dto.setHistAdolescencia(h.getHistAdolescencia());
        dto.setHistAdultez(h.getHistAdultez());
        dto.setUsoSustancias(h.getUsoSustancias());
        dto.setHistFamiliarMadre(h.getHistFamiliarMadre());
        dto.setHistFamiliarPadre(h.getHistFamiliarPadre());
        dto.setHistFamiliarHermanos(h.getHistFamiliarHermanos());
        dto.setHistFamiliarPareja(h.getHistFamiliarPareja());
        dto.setHistFamiliarHijos(h.getHistFamiliarHijos());
        h.getDiagnosticos().forEach(d -> dto.getDiagnosticos().add(
                new HistoriaClinicaDetalle.DiagnosticoItem(d.getCodCie(), d.getDescripcion())));
        h.getEvaluaciones().forEach(e -> dto.getEvaluaciones().add(
                new HistoriaClinicaDetalle.EvaluacionItem(
                        e.getNombreEvaluacion(),
                        e.getNivel(),
                        e.getPuntaje() != null ? e.getPuntaje().toPlainString() : null,
                        e.getPuntajeMax() != null ? e.getPuntajeMax().toPlainString() : null)));
        h.getPsicofarmacos().forEach(p -> dto.getPsicofarmacos().add(toPsicofarmacoItem(p)));
        h.getVisitas().forEach(v -> dto.getVisitas().add(
                new HistoriaClinicaDetalle.VisitaItem(
                        v.getFecha() != null ? v.getFecha().toString() : null,
                        v.getHora() != null ? v.getHora().toString().substring(0, 5) : null,
                        v.getTratamiento(),
                        v.getDescripcion())));

        return dto;
    }

    private static HistoriaClinicaDetalle.PsicofarmacoItem toPsicofarmacoItem(Psicofarmaco p) {
        return new HistoriaClinicaDetalle.PsicofarmacoItem(
                p.getNombre(),
                p.getDosis(),
                p.getFrecuencia(),
                p.getFechaInicio() != null ? p.getFechaInicio().toString() : null,
                p.getFechaFin() != null ? p.getFechaFin().toString() : null);
    }
}
