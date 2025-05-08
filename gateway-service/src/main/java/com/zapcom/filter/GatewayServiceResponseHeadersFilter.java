
package com.zapcom.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayServiceResponseHeadersFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    
                    // Add standard security headers
                    response.getHeaders().add("X-Content-Type-Options", "nosniff");
                    response.getHeaders().add("X-Frame-Options", "DENY");
                    response.getHeaders().add("X-XSS-Protection", "1; mode=block");
                    response.getHeaders().add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
                }));
    }

    @Override
    public int getOrder() {
        // Execute after all other filters
        return Integer.MAX_VALUE;
    }
}
