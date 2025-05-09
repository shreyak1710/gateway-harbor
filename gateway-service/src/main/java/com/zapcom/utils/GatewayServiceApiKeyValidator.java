
package com.zapcom.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GatewayServiceApiKeyValidator {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApiKeyValidator.class);
    private final WebClient webClient;
    
    public GatewayServiceApiKeyValidator(WebClient.Builder webClientBuilder, 
                                        @Value("${services.auth-service-url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
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
