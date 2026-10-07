package com.org.microservices.eureka.client.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class RatelimiterConfig {

	@Bean
	public KeyResolver ipKeyResolver() {
		
		return exchange -> {
			
			var remoteAddress = exchange.getRequest().getRemoteAddress();
			
			String ipAddress = "unknown";

			if(remoteAddress != null && remoteAddress.getAddress() != null) {
				ipAddress = remoteAddress.getAddress().getHostAddress();
			}
			
			return Mono.just(ipAddress);
		};
	}
}
