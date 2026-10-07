package com.org.help.restclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigRestclient {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		return http
				.csrf(csrf -> csrf.disable())
				
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/actuator/health", 
								"/actuator/info")
						.permitAll()
						
						.anyRequest().authenticated()
				)
				
				.oauth2ResourceServer(
						oauth -> oauth.jwt(jwt -> {})
				)
				
				.build();
	}
}
