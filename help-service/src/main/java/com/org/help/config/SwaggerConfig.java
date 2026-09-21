package com.org.help.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customSwaggerConfig() {
		
		return new OpenAPI()
				.info(new Info()
						.title("Help API")
						.version("0.0.1")
						.description("Spring Boot Application")
						.contact(new Contact()
								.name("devarajuc")
								.email("devarajuc0402@gmail.com")));
	}
}
