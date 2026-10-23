package com.org.help.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.help.service.CircuitBreakerHelpService;

@RestController
@RequestMapping("/api/help")
public class CircuitBreakerHelpController {

	@Autowired
	private CircuitBreakerHelpService test;
	
	/* Circuit breaker help-service UP check */
	@RequestMapping(value = "/message")
	public String getMessage() {
		
		return test.getTest();
//		return "Connected to help-service...";
	}
}
