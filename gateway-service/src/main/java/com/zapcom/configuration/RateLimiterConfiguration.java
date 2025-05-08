
package com.zapcom.configuration;

import com.zapcom.utils.GatewayServiceRequestConstants;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String apiKey = exchange.getRequest().getHeaders().getFirst(GatewayServiceRequestConstants.API_KEY_HEADER);
            if (apiKey != null) {
                return Mono.just(apiKey);
            }
            return Mono.just("anonymous");
        };
    }
}
