package com.springboot.proyectospring.service;

import com.springboot.proyectospring.config.DniProperties;
import com.springboot.proyectospring.dto.DniRespuestaDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class DniApiClient {

    private final DniProperties props;
    private final RestClient restClient;

    public DniApiClient(DniProperties props) {
        this.props = props;
        this.restClient = RestClient.create();
    }

    @SuppressWarnings("unchecked")
    public DniRespuestaDto consultar(String dni) {
        Map<String, Object> response = restClient.get()
                .uri(props.getUrl() + dni)
                .header("Authorization", "Bearer " + props.getToken())
                .header("Accept", "application/json")
                .retrieve()
                .body(Map.class);

        Map<String, Object> datos = response.containsKey("data")
                ? (Map<String, Object>) response.get("data")
                : response;

        String nombres   = str(datos.getOrDefault("nombres", datos.get("nombre")));
        String apPat     = str(datos.getOrDefault("apellidoPaterno", datos.get("apellido_paterno")));
        String apMat     = str(datos.getOrDefault("apellidoMaterno", datos.get("apellido_materno")));
        String apellidos = (apPat + " " + apMat).trim();

        String dir       = str(datos.getOrDefault("direccion", datos.get("domicilio")));
        String distrito  = str(datos.get("distrito"));
        String provincia = str(datos.get("provincia"));
        String residencia = buildResidencia(dir, distrito, provincia);

        String fechaRaw      = str(datos.getOrDefault("fechaNacimiento", datos.get("fecha_nacimiento")));
        String fechaNacimiento = normalizarFecha(fechaRaw);

        String sexoRaw = str(datos.getOrDefault("sexo", datos.get("genero")));
        String sexo    = mapearSexo(sexoRaw);

        return new DniRespuestaDto(nombres, apellidos, residencia, fechaNacimiento, sexo);
    }

    private String buildResidencia(String direccion, String distrito, String provincia) {
        StringBuilder sb = new StringBuilder();
        if (!direccion.isEmpty()) sb.append(direccion);
        if (!distrito.isEmpty())  sb.append(sb.isEmpty() ? "" : ", ").append(distrito);
        if (!provincia.isEmpty()) sb.append(sb.isEmpty() ? "" : " - ").append(provincia);
        return sb.toString();
    }

    /** Convierte DD/MM/YYYY → YYYY-MM-DD para el input type="date". */
    private String normalizarFecha(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        if (raw.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] p = raw.split("/");
            return p[2] + "-" + p[1] + "-" + p[0];
        }
        if (raw.matches("\\d{4}-\\d{2}-\\d{2}")) return raw;
        return raw;
    }

    /** Normaliza M/F a Masculino/Femenino. */
    private String mapearSexo(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        return switch (raw.toUpperCase()) {
            case "M", "MASCULINO" -> "Masculino";
            case "F", "FEMENINO"  -> "Femenino";
            default               -> raw;
        };
    }

    private String str(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
