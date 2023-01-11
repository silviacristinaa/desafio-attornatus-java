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
	
	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;
	
	@Test
	void whenNotFoundExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodNotFoundException(
						new NotFoundException(String.format("Person %s not found", 1l)));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(NOT_FOUND_MSG, response.getBody().getMessage());
		assertEquals("Person 1 not found", response.getBody().getErrors().get(0));
	}

	@Test
	void whenBadRequestExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodBadRequestException(
						new BadRequestException("There is already a main address, it is only possible to have one"));

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(BAD_REQUEST_MSG, response.getBody().getMessage());
		assertEquals("There is already a main address, it is only possible to have one",
				response.getBody().getErrors().get(0));
	}
}