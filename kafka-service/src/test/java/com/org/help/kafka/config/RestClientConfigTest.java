package com.org.help.kafka.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import jakarta.ws.rs.core.HttpHeaders;

@SpringBootTest
public class RestClientConfigTest {

	@Autowired
	private RestClient restClient;
	
	@Autowired
	private RestClient.Builder restClientBuilder;
	
	private static final WireMockServer wiremock = 
			new WireMockServer(
					WireMockConfiguration.DYNAMIC_PORT
			);

	static {
		wiremock.start();
	}
	
	@DynamicPropertySource
	public static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.boot.base.url", wiremock::baseUrl);
	}
	
	@BeforeEach
	public void setUp() {
		wiremock.resetAll();
		SecurityContextHolder.clearContext();
	}
	
	@AfterAll
	public static void tearDown() {
		wiremock.stop();
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void createRestclientBeans() {
		
		assertNotNull(restClient);
		assertNotNull(restClientBuilder);
	
	}
	
	@Test
	public void getResponseWithoutToken() {
		
		wiremock.stubFor(WireMock
				.get(WireMock.urlEqualTo("/api/kafka/message"))
				
				.willReturn(WireMock
						.aResponse()
						.withStatus(200)
						.withBody("Connected to help-service...")
				)
		);
		
		String response = restClient
				.get()
				.uri("/api/kafka/message")
				.retrieve()
				.body(String.class);
		
		assertThat(response)
			.isEqualTo("Connected to help-service...");
		
		wiremock.verify(1, WireMock
				.getRequestedFor(WireMock.urlEqualTo("/api/kafka/message"))
				
				.withoutHeader(HttpHeaders.AUTHORIZATION)
		);
	}
	
	@Test
	public void getResponseWithToken() {
		
		wiremock.stubFor(WireMock
				.get(WireMock.urlEqualTo("/api/kafka/message"))
				
				.willReturn(WireMock
						.aResponse()
						.withStatus(200)
						.withBody("Connected to help-service...")
				)
		);
		
		Jwt jwt = Jwt.withTokenValue("test-token")
				.header("test", "test")
				.claim("realm_access", 
						Map.of("roles", List.of("ROLE_KAFKA"))
				)
				.build();
		
		SecurityContextHolder.getContext()
			.setAuthentication(new JwtAuthenticationToken(jwt));
		
		String response = restClient
				.get()
				.uri("/api/kafka/message")
				.retrieve()
				.body(String.class);
		
		assertThat(response)
			.isEqualTo("Connected to help-service...");
		
		wiremock.verify(1, WireMock
			.getRequestedFor(WireMock.urlEqualTo("/api/kafka/message"))
			
			.withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer test-token"))
		);
	}
	
	
}
