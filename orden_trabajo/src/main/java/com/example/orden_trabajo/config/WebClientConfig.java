package com.example.orden_trabajo.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean("ClienteWebClient")

    public WebClient clienteWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082/api/v1/clientes")
                .build();
    }

    @Bean("EquipoWebClient")

    public WebClient equipoWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8083/api/v1/equipos")
                .build();
    }

}
