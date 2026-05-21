package com.example.pago.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {



    @Bean

    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8084/api/v1/ot")
                .build();
    }
}

