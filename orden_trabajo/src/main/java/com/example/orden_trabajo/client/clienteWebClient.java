package com.example.orden_trabajo.client;

import com.example.orden_trabajo.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class clienteWebClient {
    @Autowired
    @Qualifier("ClienteWebClient")
    private WebClient webClient;

    public Mono<Cliente> obtenerCliente(Long id, String token) {
        return webClient.get()
                .uri("/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Cliente no encontrado")))
                .bodyToMono(Cliente.class);
    }
}
