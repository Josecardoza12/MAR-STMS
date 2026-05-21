package com.example.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient reparacionWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8086/api/v1/reparaciones")
                .build();
    }


}
