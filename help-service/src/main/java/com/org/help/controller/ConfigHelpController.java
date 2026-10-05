package com.org.help.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
public class ConfigHelpController {

	@Value("${app.message}")
	private String message;

	@GetMapping("/config-help-test")
	public String configTest() {
		return message;
	}
}

