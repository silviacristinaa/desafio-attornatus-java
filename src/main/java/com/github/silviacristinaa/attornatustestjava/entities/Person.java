package com.github.silviacristinaa.attornatustestjava.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name; 
	@Column(nullable = false)
	private LocalDate birthDate;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	private List<Address> address;
}