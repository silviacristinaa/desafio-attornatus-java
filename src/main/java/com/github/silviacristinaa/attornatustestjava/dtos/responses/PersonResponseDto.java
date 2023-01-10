package com.github.silviacristinaa.attornatustestjava.dtos.responses;

import java.time.LocalDate;
import java.util.List;

import com.github.silviacristinaa.attornatustestjava.entities.Address;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonResponseDto {

	private Long id;
	private String name; 
	private LocalDate birthDate;
	private List<Address> address;
}