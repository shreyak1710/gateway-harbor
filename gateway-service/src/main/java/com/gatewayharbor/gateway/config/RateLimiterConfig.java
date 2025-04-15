
package com.gatewayharbor.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {
    
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // Use the IP address as the key for rate limiting
            String ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            return Mono.just(ipAddress);
        };
    }
}
