package com.org.help.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(
		name = "app.kafka.topic.enabled",
		havingValue = "true",
		matchIfMissing = true
)
public class KafkaTopicConfig {

	@Value("${spring.kafka.topic.name}")
	String topic;
	
	@Bean
	public NewTopic testTopic() {
		
		NewTopic topicBuild = TopicBuilder.name(topic)
				.partitions(3)
				.replicas(1)
				.build();
		
		return topicBuild;
	}
}
