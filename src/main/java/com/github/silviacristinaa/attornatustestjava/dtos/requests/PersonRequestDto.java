package com.github.silviacristinaa.attornatustestjava.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonRequestDto {
	
	@NotBlank
	private String name; 
	@NotNull
	private LocalDate birthDate;
}