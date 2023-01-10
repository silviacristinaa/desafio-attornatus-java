package com.github.silviacristinaa.attornatustestjava.dtos.responses;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PersonResponseDto {

	private Long id;
	private String name; 
	private LocalDate birthDate;
	private List<AddressResponseDto> address;
}