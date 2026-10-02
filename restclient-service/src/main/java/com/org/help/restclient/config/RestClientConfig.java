package com.org.help.restclient.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {
	
	@Value("${spring.boot.base.url}")
	private String baseUrl;

	@Bean
	public RestClient.Builder restClientBuilder() {
	
		return RestClient.builder();
	}
	
	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		System.out.println("Base URL: "+baseUrl);
		
		HttpClient httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(100))
				.build();
		
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(Duration.ofSeconds(100));
		
		return builder
				.requestFactory(requestFactory)
				.baseUrl(baseUrl)
				.build();
	}
}
