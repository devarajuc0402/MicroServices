package com.org.help.kafka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.org.help.entity.HelpEntity;

@Service
public class KafkaProducerService {

	private final KafkaTemplate<String, HelpEntity> kafkaTemplate;
	private final KafkaTemplate<String, List<HelpEntity>> kafkaTemplateList;

	@Value("${spring.kafka.topic.name}")
	public String topic;
	
	public KafkaProducerService(
			KafkaTemplate<String, HelpEntity> kafkaTemplate,
			KafkaTemplate<String, List<HelpEntity>> kafkaTemplateList) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaTemplateList = kafkaTemplateList;
	}
	
	public void produce(HelpEntity helpEntity) {
		
		kafkaTemplate.send(
				topic,
				String.valueOf(helpEntity.getHelpId()),
				helpEntity);
		
	}

	public void produce(List<HelpEntity> helpEntity) {

		kafkaTemplateList.send(
				topic,
				"Help_List",
				helpEntity);

	}
}
