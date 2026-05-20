package com.springboot.proyectospring.config;

import com.springboot.proyectospring.model.Especialista;
import com.springboot.proyectospring.model.Horario;
import com.springboot.proyectospring.model.PaqueteSesion;
import com.springboot.proyectospring.model.Servicio;
import com.springboot.proyectospring.model.TipoServicio;
import com.springboot.proyectospring.repository.EspecialistaRepository;
import com.springboot.proyectospring.repository.HorarioRepository;
import com.springboot.proyectospring.repository.ServicioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DatosIniciales {

    private final ServicioRepository servicioRepository;
    private final EspecialistaRepository especialistaRepository;
    private final HorarioRepository horarioRepository;

    public DatosIniciales(ServicioRepository servicioRepository,
                          EspecialistaRepository especialistaRepository,
                          HorarioRepository horarioRepository) {
        this.servicioRepository = servicioRepository;
        this.especialistaRepository = especialistaRepository;
        this.horarioRepository = horarioRepository;
    }

    @PostConstruct
    public void cargar() {
        if (!servicioRepository.findAll().isEmpty()) {
            return;
        }

        cargarServiciosGenerales();
        cargarServiciosReservables();
        cargarEspecialistas();
        cargarHorarios();
    }

    private void cargarServiciosGenerales() {
        servicioRepository.save(new Servicio(1L, "Psicoterapia Individual",
                "Tratamientos para la ansiedad, depresión, estrés, malestar emocional, fobias, entre otros problemas.",
                TipoServicio.GENERAL, null, null, null, null, null,
                "https://images.unsplash.com/photo-1573497019418-b400bb3ab074?w=800&q=80"));
        servicioRepository.save(new Servicio(2L, "Terapia familiar y de pareja",
                "Para problemas familiares y de pareja, crianza de los hijos, infidelidad, superación del duelo y otros.",
                TipoServicio.GENERAL, null, null, null, null, null,
                "https://images.unsplash.com/photo-1591343395082-e120087004b4?w=800&q=80"));
        servicioRepository.save(new Servicio(3L, "Acompañamiento adolescente",
                "Para problemas relacionados con emociones, conducta y autoestima.",
                TipoServicio.GENERAL, null, null, null, null, null,
                "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=800&q=80"));
        servicioRepository.save(new Servicio(4L, "Evaluación psicológica",
                "Evaluación de personalidad, inteligencia, dinámica familiar, psicomotricidad, neuropsicológica, entre otros.",
                TipoServicio.GENERAL, null, null, null, null, null,
                "https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=800&q=80"));
    }

    private void cargarServiciosReservables() {
        long id = 10L;
        id = guardarReservable(id, "Terapia Individual", 150.0, PaqueteSesion.UNA,
                "1 sesión de terapia individual",
                "https://images.unsplash.com/photo-1573497019418-b400bb3ab074?w=800&q=80");
        id = guardarReservable(id, "Terapia familiar y de pareja", 180.0, PaqueteSesion.UNA,
                "1 sesión de terapia familiar",
                "https://images.unsplash.com/photo-1591343395082-e120087004b4?w=800&q=80");
        id = guardarReservable(id, "Acompañamiento adolescente", 150.0, PaqueteSesion.UNA,
                "1 sesión adolescente",
                "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=800&q=80");
        id = guardarReservable(id, "Evaluación psicológica", 200.0, PaqueteSesion.UNA,
                "1 sesión de evaluación",
                "https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=800&q=80");

        id = guardarReservable(id, "Terapia Individual", 560.0, PaqueteSesion.CUATRO,
                "4 sesiones de terapia individual",
                "https://images.unsplash.com/photo-1573497019418-b400bb3ab074?w=800&q=80");
        id = guardarReservable(id, "Terapia familiar y de pareja", 680.0, PaqueteSesion.CUATRO,
                "4 sesiones de terapia familiar",
                "https://images.unsplash.com/photo-1591343395082-e120087004b4?w=800&q=80");
        id = guardarReservable(id, "Acompañamiento adolescente", 560.0, PaqueteSesion.CUATRO,
                "4 sesiones adolescente",
                "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=800&q=80");

        id = guardarReservable(id, "Terapia Individual", 1300.0, PaqueteSesion.DIEZ,
                "10 sesiones de terapia individual",
                "https://images.unsplash.com/photo-1573497019418-b400bb3ab074?w=800&q=80");
        guardarReservable(id, "Terapia familiar y de pareja", 1600.0, PaqueteSesion.DIEZ,
                "10 sesiones de terapia familiar",
                "https://images.unsplash.com/photo-1591343395082-e120087004b4?w=800&q=80");
    }

    private long guardarReservable(long id, String nombre, double precio, PaqueteSesion paquete,
                                   String detalle, String imagen) {
        Servicio s = new Servicio(id, nombre, detalle, TipoServicio.RESERVABLE, paquete, precio,
                "45 a 50 minutos (por sesión)", "Online", detalle, imagen);
        servicioRepository.save(s);
        return id + 1;
    }

    private void cargarEspecialistas() {
        especialistaRepository.save(crearEspecialista(1L, "Giulia Hernandez", "Psicóloga clínica",
                "C.Ps.P.34659",
                "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=600&q=80",
                List.of("Especialista en duelo neonatal", "Terapia cognitivo-conductual"),
                List.of("Psicóloga Clínica por la Universidad Femenina del Sagrado Corazón, Perú.",
                        "Colegiada y Habilitada por el Colegio de Psicólogos del Perú")));

        especialistaRepository.save(crearEspecialista(2L, "Emma Aguilar", "Psicóloga clínica",
                "C.Ps.P.28934",
                "https://images.unsplash.com/photo-1594824476967-48c8b964273f?w=600&q=80",
                List.of("Terapia de pareja y familiar", "Acompañamiento adolescente"),
                List.of("Psicóloga Clínica por la Pontificia Universidad Católica del Perú.",
                        "Maestría en Psicoterapia de pareja y familia.")));

        especialistaRepository.save(crearEspecialista(3L, "Henry Cano", "Psicólogo clínico",
                "C.Ps.P.31102",
                "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=600&q=80",
                List.of("Ansiedad y depresión", "Psicoterapia individual"),
                List.of("Psicólogo Clínico por la Universidad Peruana Cayetano Heredia.",
                        "Especialización en trastornos del estado de ánimo.")));

        especialistaRepository.save(crearEspecialista(4L, "Chloe Campos", "Psicóloga clínica",
                "C.Ps.P.40015",
                "https://images.unsplash.com/photo-1551836022-deb4988cc6c0?w=600&q=80",
                List.of("Evaluación psicológica", "Psicoterapia individual"),
                List.of("Psicóloga Clínica por la Universidad de Lima.")));

        especialistaRepository.save(crearEspecialista(5L, "Gabriel Cruz", "Psicólogo clínico",
                "C.Ps.P.29871",
                "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=600&q=80",
                List.of("Adolescentes", "Conducta y autoestima"),
                List.of("Psicólogo Clínico por la Universidad San Marcos.")));

        especialistaRepository.save(crearEspecialista(6L, "Louise Duarte", "Psicóloga clínica",
                "C.Ps.P.33580",
                "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=600&q=80",
                List.of("Duelo", "Terapia familiar"),
                List.of("Psicóloga Clínica por la Universidad Ricardo Palma.")));
    }

    private Especialista crearEspecialista(Long id, String nombre, String titulo, String colegiatura,
                                           String foto, List<String> especialidades, List<String> formacion) {
        Especialista e = new Especialista();
        e.setId(id);
        e.setNombre(nombre);
        e.setTitulo(titulo);
        e.setColegiatura(colegiatura);
        e.setFotoUrl(foto);
        e.setEspecialidades(especialidades);
        e.setFormacion(formacion);
        e.setDescripcionResumen(formacion.getFirst() + " · " + colegiatura);
        return e;
    }

    private void cargarHorarios() {
        Map<Long, List<String>> horasPorEspecialista = Map.of(
                1L, Arrays.asList("10:00 am", "11:00 am", "1:00 pm", "3:00 pm", "5:00 pm", "7:00 pm"),
                2L, Arrays.asList("9:00 am", "10:00 am", "12:00 pm", "2:00 pm", "4:00 pm"),
                3L, Arrays.asList("11:00 am", "1:00 pm", "3:00 pm", "6:00 pm", "8:00 pm"),
                4L, Arrays.asList("10:00 am", "12:00 pm", "2:00 pm", "4:00 pm"),
                5L, Arrays.asList("9:00 am", "11:00 am", "3:00 pm", "5:00 pm", "7:00 pm"),
                6L, Arrays.asList("10:00 am", "1:00 pm", "4:00 pm", "6:00 pm")
        );

        long horarioId = 1L;
        for (Map.Entry<Long, List<String>> entry : horasPorEspecialista.entrySet()) {
            for (String hora : entry.getValue()) {
                horarioRepository.save(new Horario(horarioId++, entry.getKey(), hora));
            }
        }
    }
}
