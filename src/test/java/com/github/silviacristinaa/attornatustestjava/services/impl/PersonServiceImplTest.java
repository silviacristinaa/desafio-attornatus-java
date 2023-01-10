package com.github.silviacristinaa.attornatustestjava.services.impl;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.PersonResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PersonServiceImplTest {

	private static final long ID = 1l;
	private static final String NAME = "Test";
	private static final int INDEX = 0;

	private PersonRequestDto personRequestDto;
	private PersonResponseDto personResponseDto;

	private Person person;

	@InjectMocks
	private PersonServiceImpl personServiceImpl;

	@Mock
	private PersonRepository personRepository;

	@Mock
	private ModelMapper modelMapper;

	@BeforeEach
	void setUp() {
		personRequestDto = new PersonRequestDto(NAME, LocalDate.now());

		personResponseDto = new PersonResponseDto(ID, NAME, LocalDate.now(), new ArrayList<>());

		person = new Person(ID, NAME, LocalDate.now(), new ArrayList<Address>());
	}

	@Test
	void whenCreateReturnSuccess() {
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(person);
		when(personRepository.save(Mockito.any())).thenReturn(person);

		Person response = personServiceImpl.create(personRequestDto);

		assertNotNull(response);
		assertEquals(Person.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(LocalDate.now(), response.getBirthDate());
		assertEquals(ArrayList.class, response.getAddress().getClass());

		verify(personRepository, times(1)).save(Mockito.any());
	}

	@Test
	void whenUpdateReturnSuccess() throws NotFoundException {
		when(personRepository.findById(Mockito.any())).thenReturn(Optional.of(person));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(person);

		personServiceImpl.update(ID, personRequestDto);

		verify(personRepository, times(1)).save(Mockito.any());
	}

	@Test
	void whenTryUpdateReturnNotFoundException() {
		when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> personServiceImpl.update(ID, personRequestDto));

		assertEquals(String.format("Person %s not found", ID), exception.getMessage());
	}

	@Test
	void whenFindByIdReturnOnePersonResponseDto() throws NotFoundException {
		when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(personResponseDto);

		PersonResponseDto response = personServiceImpl.findOnePersonById(ID);

		assertNotNull(response);

		assertEquals(PersonResponseDto.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(LocalDate.now(), response.getBirthDate());
		assertEquals(ArrayList.class, response.getAddress().getClass());
	}

	@Test
	void whenTryFindByIdReturnNotFoundException() {
		when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> personServiceImpl.findOnePersonById(ID));

		assertEquals(String.format("Person %s not found", ID), exception.getMessage());
	}

	@Test
	void whenFindAllReturnPersonResponseDtoPage() {
		when(personRepository.findAll()).thenReturn(List.of(person));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(personResponseDto);

		Page<PersonResponseDto> response = personServiceImpl.findAll(Pageable.ofSize(1));

		assertNotNull(response);
		assertEquals(1, response.getSize());
		assertEquals(PersonResponseDto.class, response.getContent().get(INDEX).getClass());

		assertEquals(ID, response.getContent().get(INDEX).getId());
		assertEquals(NAME, response.getContent().get(INDEX).getName());
		assertEquals(LocalDate.now(), response.getContent().get(INDEX).getBirthDate());
		assertEquals(ArrayList.class, response.getContent().get(INDEX).getAddress().getClass());
	}
}