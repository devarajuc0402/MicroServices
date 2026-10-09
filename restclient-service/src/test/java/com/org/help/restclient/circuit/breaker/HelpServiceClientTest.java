package com.org.help.restclient.circuit.breaker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.org.help.restclient.client.HelpServiceClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest
public class HelpServiceClientTest {

	@Autowired
	private HelpServiceClient helpServiceClient;
	
	@Autowired
	private CircuitBreakerRegistry circuitBreakerRegistry;
	
	private final static WireMockServer wiremock = 
			new WireMockServer(WireMockConfiguration.options().dynamicPort());
	
	static {
		wiremock.start();
	}
	
	@DynamicPropertySource
	public static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add("http://localhost:8081", wiremock::baseUrl);
	}
	
	@AfterAll
	public static void stopWireMock() {
		wiremock.stop();
	}
	
	@BeforeEach
	public void setUp() {
		wiremock.resetAll();
		
		CircuitBreaker breaker = 
				circuitBreakerRegistry.circuitBreaker("restclientCircuitBreaker");
		
		breaker.reset();
	}
	
	@Test
	public void successFallbackStateFailure() throws Exception {
		
		wiremock
			.stubFor(WireMock
					.get(WireMock
							.urlEqualTo("/api/help/message")
					)
					.willReturn(WireMock
							.aResponse()
							.withStatus(500)
							.withBody("Internal Server Error")
					)
			);
		
		// First 2 failures invoke the real HTTP call and fallback.
		for(int i=0;i<2;i++) {
			
			String response = helpServiceClient.getMessage();
			
			assertThat(response)
				.isEqualTo("Help Service is currently unavailable. Please try again later.");
		}
		
		// Third failure reaches the configured threshold.
		String response = helpServiceClient.getMessage();
		assertThat(response)
			.isEqualTo("Help Service is currently unavailable. Please try again later.");
		
		CircuitBreaker breaker = 
				circuitBreakerRegistry.circuitBreaker("restclientCircuitBreaker");
		
		// Check half_open state failure reaches the configured threshold.
		assertThat(breaker.getState())
			.isEqualTo(State.OPEN);
		
		// To open state configured in 30s
		Thread.sleep(Duration.ofSeconds(10));
		assertThat(breaker.getState())
			.isEqualTo(CircuitBreaker.State.HALF_OPEN);
		
	}
}
