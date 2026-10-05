package com.org.microservices.eureka.client.gateway.config;

/*
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

	public RouteLocator customRoutes(RouteLocatorBuilder builder) {
		
		return builder.routes()
				
				// help-service
				.route("help-service", r -> r
						.path("/api/help/**")
						.uri("lb://help-service"))
				
				// restclient-service
				.route("help-service", r -> r
						.path("/api/help/restclient/**")
						.uri("lb://restclient-service"))
				
				// kafka-service
				.route("help-service", r -> r
						.path("/kafka/**")
						.uri("lb://kafka-service"))

				.build();
	}
}
*/


