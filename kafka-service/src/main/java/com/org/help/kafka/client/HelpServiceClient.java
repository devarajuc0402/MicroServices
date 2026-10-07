package com.org.help.kafka.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class HelpServiceClient {

	private final RestClient restClient;
	
	public HelpServiceClient(RestClient.Builder restClient) {
		this.restClient = restClient
				.baseUrl("http://localhost:8081")
				.build();
	}
	
	@CircuitBreaker(
			name = "kafkaCircuitBreaker",
			fallbackMethod = "kafkaFallback"
	)
	public String getMessage() {
		
		return restClient
				.get()
				.uri("/api/help/message")
				.retrieve()
				.body(String.class);
	}
	
	// fallbackMethod method
	public String kafkaFallback(Throwable throwable) {
		
		System.out.println("========== FALLBACK CALLED ==========");
		System.out.println("Fallback called: " + throwable.getMessage());
		return "Help Service is currently unavailable. Please try again later.";
	}
}
