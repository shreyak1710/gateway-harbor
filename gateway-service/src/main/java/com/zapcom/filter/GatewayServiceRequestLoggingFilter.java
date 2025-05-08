
package com.zapcom.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayServiceRequestLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceRequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        String method = request.getMethod().toString();
        
        logger.info("Request: {} {}", method, path);
        
        return chain.filter(exchange)
                .doOnSuccess(aVoid -> logger.info("Response status: {}", exchange.getResponse().getStatusCode()))
                .doOnError(throwable -> logger.error("Error processing request: {}", throwable.getMessage()));
    }

    @Override
    public int getOrder() {
        // Execute before authentication filter
        return -200;
    }
}
