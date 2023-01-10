package com.github.silviacristinaa.attornatustestjava.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.PersonResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.repositories.PersonRepository;
import com.github.silviacristinaa.attornatustestjava.services.PersonService;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService{
	
	private static final String PERSON_NOT_FOUND = "Person %s not found";
	
	private final PersonRepository personRepository; 
	private final ModelMapper modelMapper;

	@Override
	@Transactional
	public Person create(PersonRequestDto personRequestDto) {
		return personRepository.save(modelMapper.map(personRequestDto, Person.class));
	}

	@Override
	@Transactional
	public void update(Long id, PersonRequestDto personRequestDto) throws NotFoundException {
		findById(id);
		
		Person person = modelMapper.map(personRequestDto, Person.class);
		person.setId(id);
		
		personRepository.save(person);
	}

	@Override
	public PersonResponseDto findOnePersonById(Long id) throws NotFoundException {
		Person person = findById(id); 
		return modelMapper.map(person, PersonResponseDto.class);
	}

	@Override
	public Page<PersonResponseDto> findAll(Pageable pageable) {
		List<PersonResponseDto> response = 
				 personRepository.findAll().stream().map(person -> modelMapper.map(person, PersonResponseDto.class))
				 .collect(Collectors.toList());
			
		 final int start = (int)pageable.getOffset();
		 final int end = Math.min((start + pageable.getPageSize()), response.size());
			
		 Page<PersonResponseDto> page = new PageImpl<>(response.subList(start, end), pageable, response.size());
		 return page;
	}
	
	private Person findById(Long id) throws NotFoundException {
		return personRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format(PERSON_NOT_FOUND, id)));				
	}
}