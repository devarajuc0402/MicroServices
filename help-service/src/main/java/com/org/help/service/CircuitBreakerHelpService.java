package com.org.help.service;

import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerHelpService {

	public String getTest() {
		
		return "Connected to help-service...";
	}
}
