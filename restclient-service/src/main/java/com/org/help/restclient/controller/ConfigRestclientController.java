package com.org.help.restclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help/restclient")
public class ConfigRestclientController {

	@Value("${restclient.app.message}")
	private String message;

	@GetMapping("/config-restclient-test")
	public String configTest() {
		return message;
	}
}

