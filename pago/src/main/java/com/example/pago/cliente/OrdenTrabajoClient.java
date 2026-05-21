package com.example.pago.cliente;

import com.example.pago.model.Orden_Trabajo;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class OrdenTrabajoClient {


    private final WebClient webClient;

    public Mono<Orden_Trabajo> obtenerOt(Long id, String token) {
        return webClient.get()
                .uri("/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "OT no encontrada")))
                .bodyToMono(Orden_Trabajo.class);
    }
}
