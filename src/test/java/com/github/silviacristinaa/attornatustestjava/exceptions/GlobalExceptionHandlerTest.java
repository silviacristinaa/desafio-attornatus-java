package com.github.silviacristinaa.attornatustestjava.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {

	private static final String NOT_FOUND_MSG = "Not found";
	private static final String BAD_REQUEST_MSG = "Bad request";
	private static final String THERE_IS_ALREADY_A_MAIN_ADDRESS =
			"There is already a main address, it is only possible to have one";
	private static final String PERSON_NOT_FOUND = "Person %s not found";

	private static final long ID = 1l;
	private static final int INDEX = 0;

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;
	
	@Test
	void whenNotFoundExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodNotFoundException(
						new NotFoundException(String.format(PERSON_NOT_FOUND, ID)));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(NOT_FOUND_MSG, response.getBody().getMessage());
		assertEquals(String.format(PERSON_NOT_FOUND, ID), response.getBody().getErrors().get(INDEX));
	}

	@Test
	void whenBadRequestExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodBadRequestException(
						new BadRequestException(THERE_IS_ALREADY_A_MAIN_ADDRESS));

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(BAD_REQUEST_MSG, response.getBody().getMessage());
		assertEquals(THERE_IS_ALREADY_A_MAIN_ADDRESS, response.getBody().getErrors().get(INDEX));
	}
}