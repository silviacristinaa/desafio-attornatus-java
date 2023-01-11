package com.github.silviacristinaa.attornatustestjava.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.PersonResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;

public interface PersonService {
	
	Person create(PersonRequestDto personRequestDto);
	
	void update(Long id, PersonRequestDto personRequestDto) throws NotFoundException;
	
	PersonResponseDto findOnePersonById(Long id) throws NotFoundException;
	
	Page<PersonResponseDto> findAll(Pageable pageable);
}