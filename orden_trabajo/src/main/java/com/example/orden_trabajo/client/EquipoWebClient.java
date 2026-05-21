package com.example.orden_trabajo.client;

import com.example.orden_trabajo.model.Equipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@Service

public class EquipoWebClient {
    @Autowired
    @Qualifier("EquipoWebClient")
    private  WebClient webClient;

    public Mono<Equipo> obtenerEquipo(Long id, String token) {
        return webClient.get()
                .uri("/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Equipo no encontrado")))
                .bodyToMono(Equipo.class);
    }
}
