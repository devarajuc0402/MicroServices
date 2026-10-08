package com.org.help.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigHelp {

	@Autowired
	JwtTokenRoleConverter jwtTokenRoleConverter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		return http
				
				.csrf(csrf -> csrf.disable())
				
				.authorizeHttpRequests(exchange -> exchange
						
						.requestMatchers(
								"/actuator/health",
								"/actuator/info"
						).permitAll()
						
						.requestMatchers("/api/help/**").hasAnyRole("USER", "HELP")
						
						.anyRequest().authenticated()
				)
				
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt
								.jwtAuthenticationConverter(jwtTokenRoleConverter.jwtAuthenticationConverter()))
				)
				.build();
	}
}
