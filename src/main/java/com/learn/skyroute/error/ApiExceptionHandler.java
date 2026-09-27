package com.learn.skyroute.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> notFound(ResourceNotFoundException exception) {
		return error(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ApiError> invalid(InvalidRequestException exception) {
		return error(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	private ResponseEntity<ApiError> error(HttpStatus status, String message) {
		ApiError body = new ApiError(status.value(), status.getReasonPhrase(), message);
		return ResponseEntity.status(status).body(body);
	}

}
