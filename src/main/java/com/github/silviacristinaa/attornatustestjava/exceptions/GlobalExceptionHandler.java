package com.github.silviacristinaa.attornatustestjava.exceptions;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String EXCEPTION_MSG_UNEXPECTED_ERROR = "Unexpected error";
	private static final String EXCEPTION_MSG_ARGUMENTS_NOT_VALID = "Arguments not valid";
	private static final String NOT_FOUND_MSG = "Not found";

	private static final String BAD_REQUEST_MSG = "Bad request";

	private static final String EXCEPTION_LOG_MSG = "e=%s,m=%s";
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorMessage> handleMethodException(final Exception ex) {
		logE(ex);
		
		return new ResponseEntity<>(ErrorMessage.builder().message(EXCEPTION_MSG_UNEXPECTED_ERROR)
				.errors(Arrays.asList(ex.getMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleMethodBadRequestException(final MethodArgumentNotValidException ex) {
		logE(ex);
		
		return new ResponseEntity<>(ErrorMessage.builder().message(EXCEPTION_MSG_ARGUMENTS_NOT_VALID)
				.errors(Arrays.asList(ex.getMessage())).build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorMessage> handleMethodNotFoundException(final NotFoundException ex) {
		logE(ex);
		
		final ErrorMessage errorMessage = ErrorMessage.builder().message(NOT_FOUND_MSG)
				.errors(Arrays.asList(ex.getMessage())).build();
		return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorMessage> handleMethodBadRequestException(final BadRequestException ex) {
		logE(ex);

		final ErrorMessage errorMessage = ErrorMessage.builder().message(BAD_REQUEST_MSG)
				.errors(Arrays.asList(ex.getMessage())).build();
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
	
	private static void logE(final Exception e) {
	    final String message = String.format(EXCEPTION_LOG_MSG, e.getClass().getSimpleName(), e.getMessage());
	    log.error(message, e);
	}
}