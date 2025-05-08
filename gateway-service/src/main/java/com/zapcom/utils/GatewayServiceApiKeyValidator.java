
package com.zapcom.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GatewayServiceApiKeyValidator {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApiKeyValidator.class);
    private final WebClient webClient;

    public GatewayServiceApiKeyValidator(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public Mono<Boolean> validateApiKey(String apiKey) {
        logger.info("Validating API key");
        
        return webClient
                .post()
                .uri("/api/auth/validate-api-key")
                .bodyValue(new ApiKeyRequest(apiKey))
                .retrieve()
                .bodyToMono(ApiKeyResponse.class)
                .map(ApiKeyResponse::isValid)
                .onErrorReturn(false)
                .doOnNext(valid -> {
                    if (valid) {
                        logger.info("API key validation successful");
                    } else {
                        logger.error("API key validation failed");
                    }
                });
    }

    // Simple request/response objects for the validation API
    private record ApiKeyRequest(String apiKey) {}
    private record ApiKeyResponse(boolean valid) {}
}
