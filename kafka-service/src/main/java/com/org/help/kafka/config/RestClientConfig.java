package com.org.help.kafka.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
	
	@Value("${spring.boot.base.url}")
	private String baseUrl;

	@Bean
//	@LoadBalanced
	public RestClient.Builder restClientBuilder() {
	
		return RestClient.builder()
				
				.requestInterceptor((request, body, execution) -> {
					
					Authentication authentication = 
							SecurityContextHolder.getContext().getAuthentication();
				
					if(authentication instanceof JwtAuthenticationToken jwtAuth) {
						
						String token = jwtAuth.getToken().getTokenValue();
						
						request.getHeaders().setBearerAuth(token);
					}

					return execution.execute(request, body);
				});

	}
	
	@Bean
	RestClient restClientBuild(RestClient.Builder builder) {
		
		return builder.build();
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

