package com.example.microservicioviaje.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().add("X-User-Role", "SYSTEM");
                    return execution.execute(request, body);
                })
                .build();
    }
}

