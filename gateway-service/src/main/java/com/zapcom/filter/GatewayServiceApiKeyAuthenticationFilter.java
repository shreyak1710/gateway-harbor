
package com.zapcom.filter;

import com.zapcom.exception.GatewayServiceInvalidApiKeyException;
import com.zapcom.exception.GatewayServiceMissingHeaderException;
import com.zapcom.utils.GatewayServiceApiKeyValidator;
import com.zapcom.utils.GatewayServicePathConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class GatewayServiceApiKeyAuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApiKeyAuthenticationFilter.class);
    private static final String API_KEY_HEADER = "X-API-Key";
    
    private final GatewayServiceApiKeyValidator apiKeyValidator;
    
    public GatewayServiceApiKeyAuthenticationFilter(GatewayServiceApiKeyValidator apiKeyValidator) {
        this.apiKeyValidator = apiKeyValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        
        // Skip API key validation for open endpoints
        if (isOpenPath(path)) {
            logger.debug("Skipping API key validation for open path: {}", path);
            return chain.filter(exchange);
        }
        
        // Check for API key in header
        List<String> apiKeys = request.getHeaders().get(API_KEY_HEADER);
        if (apiKeys == null || apiKeys.isEmpty()) {
            logger.error("Missing API key header for request to {}", path);
            return Mono.error(new GatewayServiceMissingHeaderException("API key header is required"));
        }
        
        String apiKey = apiKeys.get(0);
        
        return apiKeyValidator.validateApiKey(apiKey)
                .flatMap(isValid -> {
                    if (!isValid) {
                        logger.error("Invalid API key for request to {}", path);
                        return Mono.error(new GatewayServiceInvalidApiKeyException("Invalid API key"));
                    }
                    
                    logger.info("API key validated successfully for request to {}", path);
                    return chain.filter(exchange);
                });
    }

    private boolean isOpenPath(String path) {
        return Arrays.stream(GatewayServicePathConstants.OPEN_API_ENDPOINTS)
                .anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        // Set high priority for this filter
        return -100;
    }
}
