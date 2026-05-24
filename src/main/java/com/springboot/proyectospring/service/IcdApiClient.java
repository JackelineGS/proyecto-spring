package com.springboot.proyectospring.service;

import com.springboot.proyectospring.config.IcdProperties;
import com.springboot.proyectospring.dto.IcdResultadoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IcdApiClient {

    private final IcdProperties props;
    private final IcdTokenService tokenService;
    private final RestClient restClient;

    public IcdApiClient(IcdProperties props, IcdTokenService tokenService) {
        this.props = props;
        this.tokenService = tokenService;
        this.restClient = RestClient.create();
    }

    @SuppressWarnings("unchecked")
    public List<IcdResultadoDto> buscar(String texto) {
        try {
            Map<String, Object> resultado = restClient.get()
                    .uri(props.getBaseUrl() + "/icd/release/11/2024-01/mms/search?q={q}&highlighted=false", texto)
                    .header("Authorization", "Bearer " + tokenService.getToken())
                    .header("Accept", "application/json")
                    .header("Accept-Language", "es")
                    .header("API-Version", "v2")
                    .retrieve()
                    .body(Map.class);

            List<Map<String, Object>> entidades =
                    (List<Map<String, Object>>) resultado.get("destinationEntities");
            if (entidades == null) return List.of();

            return entidades.stream()
                    .map(e -> new IcdResultadoDto(
                            (String) e.get("theCode"),
                            limpiarHtml((String) e.get("title"))
                    ))
                    .filter(r -> r.getCodigo() != null)
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    private String limpiarHtml(String texto) {
        return texto == null ? "" : texto.replaceAll("<[^>]+>", "");
    }
}
