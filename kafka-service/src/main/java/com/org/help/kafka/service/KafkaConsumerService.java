package com.org.help.kafka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.org.help.kafka.entity.HelpEntity;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@KafkaListener(
			topics = "help-topic",
			groupId = "help-group")
	public void consume(String help) {
		
		JsonNode jsonNode = objectMapper.readTree(help);
		
		System.out.println(jsonNode);
		
		if(jsonNode.isObject()) {
			
			HelpEntity helpEntity = objectMapper.treeToValue(
					jsonNode, 
					HelpEntity.class);
			System.out.println(helpEntity);
			
		} else if(jsonNode.isArray()) {
			
			List<HelpEntity> helpList = objectMapper.treeToValue(
					jsonNode, 
					new TypeReference<List<HelpEntity>>() {});
			
			System.out.println(helpList);
		} 
		
	}
	
}
