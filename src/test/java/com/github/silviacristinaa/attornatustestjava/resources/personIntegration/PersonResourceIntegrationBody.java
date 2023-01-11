package com.github.silviacristinaa.attornatustestjava.resources.personIntegration;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;

import java.time.LocalDate;

public class PersonResourceIntegrationBody {
    public static PersonRequestDto personBadRequest() {
        return new PersonRequestDto(null, LocalDate.now());
    }

    public static PersonRequestDto personCreateSuccess(LocalDate date) {
        return new PersonRequestDto("Test", date);
    }
}