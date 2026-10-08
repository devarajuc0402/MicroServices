package com.org.microservices.eureka.client.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfigGateway {

	@Autowired
	JwtTokenRoleConverter jwtTokenRoleConverter;
	
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(
			ServerHttpSecurity http) {
		
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeExchange(exchange -> exchange
						.pathMatchers(
								"/actuator/health",
								"/actuator/info")
						.permitAll()

						.pathMatchers("/actuator/gateway/**").hasRole("ADMIN")
						
						.pathMatchers("/config-api-gateway-test").hasRole("ADMIN")
						
						.pathMatchers("/fallback/**").hasRole("USER")

						.anyExchange()
						.authenticated()
				)
				
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt
								.jwtAuthenticationConverter(
										jwtTokenRoleConverter.jwtAuthenticationConverter())
						)
				)
				.build();
	}
	
}
