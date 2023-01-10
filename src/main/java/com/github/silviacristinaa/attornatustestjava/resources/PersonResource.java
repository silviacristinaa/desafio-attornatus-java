package com.github.silviacristinaa.attornatustestjava.resources;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.PersonResponseDto;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.services.PersonService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/people")
@RequiredArgsConstructor
public class PersonResource {
	
	private static final String ID = "/{id}";
	
	private final PersonService personService; 
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<Void> create(@RequestBody @Valid PersonRequestDto personRequestDto) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path(ID).buildAndExpand(personService.create(personRequestDto).getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = ID)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid PersonRequestDto personRequestDto) throws NotFoundException {
		personService.update(id, personRequestDto);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value = ID)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<PersonResponseDto> findById(@PathVariable Long id) throws NotFoundException {
		return ResponseEntity.ok(personService.findOnePersonById(id));
	}
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<Page<PersonResponseDto>> findAll(Pageable pageable) {
		return ResponseEntity.ok(personService.findAll(pageable));
	}
}