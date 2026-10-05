package com.org.help.kafka.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class ConfigKafkaController {

	@Value("${kafka.app.message}")
	private String message;

	@GetMapping("/config-kafka-test")
	public String configTest() {
		return message;
	}
}

