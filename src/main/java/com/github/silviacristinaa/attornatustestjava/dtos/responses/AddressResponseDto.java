package com.github.silviacristinaa.attornatustestjava.dtos.responses;

import com.github.silviacristinaa.attornatustestjava.entities.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AddressResponseDto {
	
	private Long id;
	private String publicPlace;
	private String zipCode;
	private Long number;
	private String city;
	private boolean isMain;
}