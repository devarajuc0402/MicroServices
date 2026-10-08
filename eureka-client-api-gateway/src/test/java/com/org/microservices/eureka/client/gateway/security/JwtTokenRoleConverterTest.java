package com.org.microservices.eureka.client.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.org.microservices.eureka.client.gateway.config.JwtTokenRoleConverter;

import reactor.core.publisher.Mono;

public class JwtTokenRoleConverterTest {

	private final JwtTokenRoleConverter jwtTokenRoleConverter = 
			new JwtTokenRoleConverter();
	
	@Test
	public void shouldConvertFromRoles() {
		
		Jwt jwt = Jwt.withTokenValue("test-token")
				.header("alg", "RS256")
				.claim("realm_access", 
						Map.of("roles", List.of("ADMIN", "USER")))
				.build();

		Converter<Jwt, Mono<AbstractAuthenticationToken>> converter = jwtTokenRoleConverter.jwtAuthenticationConverter();

		AbstractAuthenticationToken authentication = converter.convert(jwt).block();
		
		assertThat(authentication)
			.isNotNull();

		assertThat(authentication.getAuthorities())
			.extracting(GrantedAuthority::getAuthority)
			.containsExactlyInAnyOrder(
                    "ROLE_USER",
                    "ROLE_ADMIN",
                    "FACTOR_BEARER"
            );

	}
}
