package com.example.inventario.client;

import com.example.inventario.model.Repuesto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class ReparacionClient {

    private final WebClient webClient;


    public Mono<Repuesto> obtenerReparacion(Long id, String token) {
        return webClient.get()
                .uri("/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Reparacion no encontrado")))
                .bodyToMono(Repuesto.class);
    }
}
