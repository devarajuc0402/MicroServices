package com.org.help.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public RedisCacheManager cacheManager(
			RedisConnectionFactory connection) {
		
		BasicPolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
				.allowIfBaseType(Object.class)
				.build();
		
		GenericJacksonJsonRedisSerializer jsonserializer = 
				GenericJacksonJsonRedisSerializer.builder()
				.enableDefaultTyping(validator)
				.build();
		
		RedisCacheConfiguration config = 
				RedisCacheConfiguration.defaultCacheConfig()
		
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
					new StringRedisSerializer()
			))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
					jsonserializer
			));

		return RedisCacheManager.builder(connection)
				.cacheDefaults(config)
				.build();
	}
	
	
}
