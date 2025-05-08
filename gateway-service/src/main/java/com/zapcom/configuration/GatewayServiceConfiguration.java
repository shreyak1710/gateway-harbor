
package com.zapcom.configuration;

import com.zapcom.filter.GatewayServiceApiKeyAuthenticationFilter;
import com.zapcom.filter.GatewayServiceRequestLoggingFilter;
import com.zapcom.filter.GatewayServiceResponseHeadersFilter;
import com.zapcom.utils.GatewayServicePathConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayServiceConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceConfiguration.class);

	@Value("${services.customer-service-url}")
	private String customerServiceUrl;

	@Value("${services.auth-service-url}")
	private String authServiceUrl;

	@Autowired
	private GatewayServiceApiKeyAuthenticationFilter gatewayServiceApiKeyAuthenticationFilter;

	@Autowired
	private GatewayServiceRequestLoggingFilter gatewayServiceRequestLoggingFilter;

	@Autowired
	private GatewayServiceResponseHeadersFilter gatewayServiceResponseHeadersFilter;

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		logger.info("Configuring API Gateway routes with static service discovery");

		return builder.routes()
				// Auth service route
				.route("auth-service", r -> {
					logger.info("Configuring auth-service route for path: {}", GatewayServicePathConstants.AUTH_PATH + "/**");
					return r.path(GatewayServicePathConstants.AUTH_PATH + "/**")
							.filters(f -> f
									.rewritePath(GatewayServicePathConstants.AUTH_PATH + "/(?<segment>.*)", "/auth/${segment}")
									.filter(gatewayServiceRequestLoggingFilter)
									.filter(gatewayServiceResponseHeadersFilter))
							.uri(authServiceUrl);
				})

				// Customer service route (protected with API Key)
				.route("customer-service", r -> {
					logger.info("Configuring customer-service route for path: {}", GatewayServicePathConstants.CUSTOMER_PATH + "/**");
					return r.path(GatewayServicePathConstants.CUSTOMER_PATH + "/**")
							.filters(f -> f
									.rewritePath(GatewayServicePathConstants.CUSTOMER_PATH + "/(?<segment>.*)", "/customers/${segment}")
									.filter(gatewayServiceApiKeyAuthenticationFilter)
									.filter(gatewayServiceRequestLoggingFilter)
									.filter(gatewayServiceResponseHeadersFilter))
							.uri(customerServiceUrl);
				})
				.build();
	}
}
