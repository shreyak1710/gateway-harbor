
package com.gatewayharbor.gateway.filter;

import com.gatewayharbor.gateway.service.ApiKeyValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends AbstractGatewayFilterFactory<ApiKeyAuthenticationFilter.Config> {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);
    private static final String API_KEY_HEADER = "X-API-Key";
    
    private final ApiKeyValidationService apiKeyValidationService;
    
    // Paths that don't require API key authentication
    private final List<String> openApiEndpoints = List.of(
            "/auth/api/auth/register",
            "/auth/api/auth/verify-email",
            "/auth/api/auth/login",
            "/auth/api/auth/generate-api-key",
            "/fallback"
    );

    public ApiKeyAuthenticationFilter(ApiKeyValidationService apiKeyValidationService) {
        super(Config.class);
        this.apiKeyValidationService = apiKeyValidationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            
            // Skip API key validation for open endpoints
            if (openApiEndpoints.stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }
            
            // Check for API key in header
            List<String> apiKeys = request.getHeaders().get(API_KEY_HEADER);
            if (apiKeys == null || apiKeys.isEmpty()) {
                logger.error("Missing API key for request to {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String apiKey = apiKeys.get(0);
            
            return apiKeyValidationService.validateApiKey(apiKey)
                    .flatMap(isValid -> {
                        if (!isValid) {
                            logger.error("Invalid API key for request to {}", path);
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                        
                        logger.info("API key validated successfully for request to {}", path);
                        return chain.filter(exchange);
                    });
        };
    }

    public static class Config {
        // Configuration properties if needed
    }
}
