package com.org.microservices.eureka.client.gateway.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;

import com.org.microservices.eureka.client.gateway.config.RatelimiterConfig;

public class RatelimiterConfigTest {

	private final RatelimiterConfig ratelimiterConfig = 
			new RatelimiterConfig();
	
	@Test
	public void ipKeyResolverTest() {
		
		KeyResolver KeyResolver = ratelimiterConfig.ipKeyResolver();
		
		assertThat(KeyResolver)
			.isNotNull();
	}
}
