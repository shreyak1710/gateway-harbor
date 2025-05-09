
package com.gatewayharbor.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApiKeyValidationService {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyValidationService.class);
    private final WebClient webClient;

    public ApiKeyValidationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public Mono<Boolean> validateApiKey(String apiKey) {
        logger.info("Validating API key");
        
        return webClient
                .post()
                .uri("/api/auth/validate-api-key")
                .bodyValue(new java.util.HashMap<String, String>() {{ put("apiKey", apiKey); }})
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .map(response -> (Boolean) response.get("valid"))
                .onErrorReturn(false)
                .doOnNext(valid -> {
                    if (valid) {
                        logger.info("API key validation successful");
                    } else {
                        logger.error("API key validation failed");
                    }
                });
    }
}
