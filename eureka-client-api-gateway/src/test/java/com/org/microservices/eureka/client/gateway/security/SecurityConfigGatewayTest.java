package com.org.microservices.eureka.client.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.org.microservices.eureka.client.gateway.config.JwtTokenRoleConverter;

import reactor.core.publisher.Mono;

@SpringBootTest
public class SecurityConfigGatewayTest {

	@Autowired
	private ApplicationContext context;
	
	private WebTestClient webTestClient;
	
	
	@BeforeEach
	public void setUp() {
		
		webTestClient = WebTestClient
				.bindToApplicationContext(context)
				.apply(SecurityMockServerConfigurers.springSecurity())
				.configureClient()
				.build();
	}
	
	@Test
	public void successResponse() {
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers
					.mockJwt()
					.authorities(
							new SimpleGrantedAuthority("ROLE_ADMIN")
					)
			)
			.get()
			.uri("/config-api-gateway-test")
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	@Test
	public void successFallback503Error() {
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers
					.mockJwt()
					.authorities(
							new SimpleGrantedAuthority("ROLE_USER")
					)
			)
			.get()
			.uri("/fallback/help")
			.exchange()
			.expectStatus()
			.is5xxServerError();
	}
	
	@Test
	public void getJwt401Error() {
		
		webTestClient
				.get()
				.uri("/api/help/message")
				.exchange()
				.expectStatus()
				.isUnauthorized();
	}
	
	@Test
	public void get404NotFound() {
		
		JwtTokenRoleConverter jwtTokenRoleConverter = 
				new JwtTokenRoleConverter();
		
		Jwt jwt = Jwt.withTokenValue("test-token")
				.header("alg", "RS256")
				.claim("realm_access", 
						Map.of("roles", List.of("ADMIN", "USER")))
				.build();

		Converter<Jwt, Mono<AbstractAuthenticationToken>> converter = jwtTokenRoleConverter.jwtAuthenticationConverter();

		AbstractAuthenticationToken authentication = converter.convert(jwt).block();
		
		assertThat(authentication).isNotNull();

		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockAuthentication(authentication))
//			.mutateWith(SecurityMockServerConfigurers.mockJwt())
			.get()
			.uri("/api/help_not_found/not_found")
			.exchange()
			.expectStatus()
			.isNotFound();
	}
}
