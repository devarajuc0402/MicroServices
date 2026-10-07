package com.org.help.kafka.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigKafka {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		return http
				.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(exchange -> exchange

						.requestMatchers(
								"/actuator/health",
								"/actuator/info"
						).permitAll()
						
						.anyRequest().authenticated()
				)
				
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> {})
				)
				.build();
	}
}
