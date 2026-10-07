package com.org.help.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
public class CircuitBreakerHelpController {

	/* Circuit breaker help-service UP check */
	@RequestMapping(value = "/message")
	public String getMessage() {
		
		return "Connected to help-service...";
	}
}
