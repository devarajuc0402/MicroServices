package com.org.help.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Value("${rest.client.base.url}")
	private String baseUrl;
	
	// Because calling in same application It I/O blocking thread deadlock
	// client thread block waiting for a response but server doesn't have no thread to process RestClient
	
	@Bean
	public RestClient.Builder restClientBuilder() {
		
		return RestClient.builder();
		
	}
	
	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		
		return builder.baseUrl(baseUrl)
				.build();
	}
}
