package com.org.help.kafka.circuit.breaker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.org.help.kafka.client.HelpServiceClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest
public class HelpServiceClientTest {
	
	@Value("${spring.boot.base.url}")
	private String baseUrl;
	
	@Autowired
	private HelpServiceClient helpServiceClient;
	
	@Autowired
	private CircuitBreakerRegistry circuitBreakerRegistry;
	
	private final static WireMockServer wireMock = 
			new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
	
	static {
		wireMock.start();
	}
	
	@DynamicPropertySource
	public static void registry(DynamicPropertyRegistry registry) {
		registry.add("spring.boot.base.url", wireMock::baseUrl);
	}
	
	@BeforeEach
	public void setUp() {
		wireMock.resetAll();
		
		SecurityContextHolder.clearContext();
		
		CircuitBreaker circuitBreaker = 
				circuitBreakerRegistry.circuitBreaker("kafkaCircuitBreaker");
		
		circuitBreaker.reset();
	}
	
	@AfterAll
	public static void tearDown() {
		wireMock.stop();
	}
	
	@Test
	public void fallBackFailure() throws Exception {
		
		System.out.println("baseUrl: "+baseUrl);
		
		wireMock.stubFor(WireMock
				.get("/api/help/message")
				.willReturn(WireMock
						.aResponse()
						.withStatus(500)
						.withBody("Internal Server Error")
				)
		);
		
		SecurityContextHolder.clearContext();
		
		// call http request 2 times after moving to fallback
		for(int i=0;i<2;i++) {
			String response = helpServiceClient.getMessage();
			
			assertThat(response)
				.isEqualTo("Help Service is currently unavailable. Please try again later.");
		}
		
		// 3rd request move across to theshold changing state
		String response = helpServiceClient.getMessage();
		
		assertThat(response)
			.isEqualTo("Help Service is currently unavailable. Please try again later.");
		
		// checking open state
		CircuitBreaker breaker = circuitBreakerRegistry
				.circuitBreaker("kafkaCircuitBreaker");
		
		assertThat(breaker.getState())
			.isEqualTo(State.OPEN);
		
		
		// after 20s its changing to half_open
		Thread.sleep(Duration.ofSeconds(22));
		assertThat(breaker.getState())
			.isEqualTo(State.HALF_OPEN);
		
	}
}

