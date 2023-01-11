package com.github.silviacristinaa.attornatustestjava.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PersonRequestDto {
	
	@NotBlank
	private String name; 
	@NotNull
	private LocalDate birthDate;
}