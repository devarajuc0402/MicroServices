package com.org.help.restclient.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	private static final WireMockServer wireMock = 
			new WireMockServer(WireMockConfiguration
					.wireMockConfig()
					.dynamicPort()
			);
	
	static {
		wireMock.start();
	}
	
	@DynamicPropertySource
	public static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.boot.base.url", wireMock::baseUrl);
	}
	
	@BeforeEach
	public void setUp() {
		wireMock.resetAll();
		SecurityContextHolder.clearContext();
	}
	
	@AfterAll
	public static void tearDown() {
		wireMock.stop();
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void createRestclientBeans() {
		
		assertNotNull(restClient);
		assertNotNull(restClientBuilder);
		
	}
	
	@Test
	public void getForwordJwtToken() {
		
		wireMock.stubFor(WireMock
				.get(WireMock.urlEqualTo("/api/restclient/message"))
				
				.willReturn(WireMock
						.aResponse()
						.withStatus(200)
						.withBody("Connected to help-service...")
				)
		);
		
		restClient = RestClient.builder()
				.requestInterceptor((request, body, execution) -> {
				
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					
					if(authentication instanceof JwtAuthenticationToken jwt) {
						
						request.getHeaders()
							.setBearerAuth(
									jwt.getToken().getTokenValue()
									
							);
					}
					return execution.execute(request, body);
				})
				
				.baseUrl(wireMock.baseUrl())
				.build();
		
		String response = restClient
				.get()
				.uri("/api/restclient/message")
				.retrieve()
				.body(String.class);
		
		assertThat(response)
			.isEqualTo("Connected to help-service...");
		
		wireMock.verify(1, WireMock
				.getRequestedFor(WireMock
						.urlEqualTo("/api/restclient/message"))
				
				.withoutHeader(HttpHeaders.AUTHORIZATION)
			);
	}
}
