package com.rhaydae.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleUserNotFound(UserNotFoundException ex) { 
		return new ErrorResponse( HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now() ); 
		}


	@ExceptionHandler(EmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleEmailAlreadyExists( EmailAlreadyExistsException ex) { 
	return new ErrorResponse( HttpStatus.CONFLICT.value(), ex.getMessage(), LocalDateTime.now() ); 
	}
	
	public record ErrorResponse( 
			int status, 
			String message, 
			LocalDateTime timestamp ) {
	}
	}