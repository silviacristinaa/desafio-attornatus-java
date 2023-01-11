package com.github.silviacristinaa.attornatustestjava.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String publicPlace;
	@Column(nullable = false)
	private String zipCode;
	@Column(nullable = false)
	private Long number;
	@Column(nullable = false)
	private String city;
	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;
	@Column
	private boolean isMain;
}