package com.org.microservices.eureka.client.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public ResponseEntity<?> helpFallback() {
		
		return ResponseEntity
				.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Help Service is currently unavailable. Please try again later.");
	}
	
	@RequestMapping(value = "/restclient", method = RequestMethod.GET)
	public ResponseEntity<?> restclientFallback() {
		
		return ResponseEntity
				.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Restclient Service is currently unavailable. Please try again later.");
	}
	
	@RequestMapping(value = "/kafka", method = RequestMethod.GET)
	public ResponseEntity<?> kafkaFallback() {
		
		return ResponseEntity
				.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Kafka Service is currently unavailable. Please try again later.");
	}
}
