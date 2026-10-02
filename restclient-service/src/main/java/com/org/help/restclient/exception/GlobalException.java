package com.org.help.restclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResource(ResourceNotFoundException resourceNotFoundException) {
		return ResponseEntity.status(
				HttpStatus.NOT_FOUND)
				.body("Resource Not Found: "+resourceNotFoundException);
	}
}
