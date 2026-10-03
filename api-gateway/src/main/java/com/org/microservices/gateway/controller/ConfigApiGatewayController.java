package com.org.microservices.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigApiGatewayController {

	@Value("${api.gateway.app.message}")
	private String message;

	@GetMapping("/config-api-gateway-test")
	public String configTest() {
		return message;
	}
}

