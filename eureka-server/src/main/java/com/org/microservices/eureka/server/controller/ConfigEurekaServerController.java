package com.org.microservices.eureka.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigEurekaServerController {

	@Value("${eureka.server.app.message}")
	private String message;

	@GetMapping("/config-eureka-server-test")
	public String configTest() {
		return message;
	}
}

