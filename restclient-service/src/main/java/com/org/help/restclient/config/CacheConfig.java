package com.org.help.restclient.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {
	
	@Bean
	public RedisCacheManager cacheManager(
			RedisConnectionFactory connectionFactory) {
		
		GenericJacksonJsonRedisSerializer jsonSerializer = GenericJacksonJsonRedisSerializer.builder().build();
		
		RedisCacheConfiguration config = 
				RedisCacheConfiguration.defaultCacheConfig()
				
					.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
							new StringRedisSerializer()))
					
					.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
							jsonSerializer));
		
		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(config)
				.build();
		
	}
}
