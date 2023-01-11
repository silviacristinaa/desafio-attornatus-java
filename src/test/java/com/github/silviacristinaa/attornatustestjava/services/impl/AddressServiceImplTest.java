package com.github.silviacristinaa.attornatustestjava.services.impl;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressIsMainRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.exceptions.BadRequestException;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.repositories.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AddressServiceImplTest {

    private static final long ID = 1l;
    private static final String PUBLIC_PLACE = "Test";
    private static final String ZIP_CODE = "test";
    private static final long NUMBER = 1l;
    private static final String CITY = "test";
    private static final String NAME = "Test";
    private static final int INDEX = 0;

    private static final String PERSON_NOT_FOUND = "Person %s not found";
    private static final String THERE_IS_ALREADY_A_MAIN_ADDRESS =
            "There is already a main address, it is only possible to have one";

    private AddressRequestDto addressRequestDto;
    private AddressIsMainRequestDto addressIsMainRequestDto;
    private AddressResponseDto addressResponseDto;

    private Address address;
    private Person person;

    @InjectMocks
    private AddressServiceImpl addressServiceImpl;

    @Mock
    private PersonServiceImpl personServiceImpl;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        addressRequestDto = new AddressRequestDto(PUBLIC_PLACE, ZIP_CODE, NUMBER, CITY,
                true);
        addressIsMainRequestDto = new AddressIsMainRequestDto(true);
        addressResponseDto = new AddressResponseDto(ID, PUBLIC_PLACE, ZIP_CODE, NUMBER, CITY,
                true);

        person = new Person(ID, NAME, LocalDate.now(), new ArrayList<>());
        address = new Address(ID, PUBLIC_PLACE, ZIP_CODE, NUMBER, CITY, person, true);
    }

    @Test
    void whenCreateReturnSuccess() throws NotFoundException, BadRequestException {
        when(personServiceImpl.findById(Mockito.any())).thenReturn(person);
        when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(address);
        when(addressRepository.save(Mockito.any())).thenReturn(address);

        Address response = addressServiceImpl.create(ID, addressRequestDto);

        assertNotNull(response);
        assertEquals(Address.class, response.getClass());
        assertEquals(Person.class, response.getPerson().getClass());

        assertEquals(ID, response.getId());
        assertEquals(PUBLIC_PLACE, response.getPublicPlace());
        assertEquals(ZIP_CODE, response.getZipCode());
        assertEquals(NUMBER, response.getNumber());
        assertEquals(CITY, response.getCity());
        assertEquals(ID, response.getPerson().getId());
        assertEquals(NAME, response.getPerson().getName());
        assertEquals(true, response.isMain());

        verify(addressRepository, times(1)).save(Mockito.any());
    }

    @Test
    void whenTryCreateReturnNotFoundException() throws NotFoundException {
        when(personServiceImpl.findById(anyLong())).thenThrow(
                new NotFoundException(String.format(PERSON_NOT_FOUND, ID)));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> addressServiceImpl.create(ID, addressRequestDto));

        assertEquals(String.format(PERSON_NOT_FOUND, ID), exception.getMessage());
    }

    @Test
    void whenTryCreateReturnBadRequestException() throws NotFoundException {
        when(personServiceImpl.findById(Mockito.any())).thenReturn(person);
        when(addressRepository.findByPersonAndIsMainTrue(Mockito.any())).thenReturn(Optional.of(address));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> addressServiceImpl.create(ID, addressRequestDto));

        assertEquals(THERE_IS_ALREADY_A_MAIN_ADDRESS, exception.getMessage());
    }

    @Test
    void whenFindAllPersonAddressesReturnAddressResponseDtoPage() throws NotFoundException {
        when(personServiceImpl.findById(Mockito.any())).thenReturn(person);
        when(addressRepository.findByPerson(Mockito.any())).thenReturn(List.of(address));
        when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(addressResponseDto);

        Page<AddressResponseDto> response = addressServiceImpl.findAllPersonAddresses(ID, Pageable.ofSize(1));

        assertNotNull(response);
        assertEquals(1, response.getSize());
        assertEquals(AddressResponseDto.class, response.getContent().get(INDEX).getClass());

        assertEquals(ID, response.getContent().get(INDEX).getId());
        assertEquals(PUBLIC_PLACE, response.getContent().get(INDEX).getPublicPlace());
        assertEquals(ZIP_CODE, response.getContent().get(INDEX).getZipCode());
        assertEquals(NUMBER, response.getContent().get(INDEX).getNumber());
        assertEquals(CITY, response.getContent().get(INDEX).getCity());
        assertEquals(true, response.getContent().get(INDEX).isMain());
    }

    @Test
    void whenTryFindAllPersonAddressesReturnNotFoundException() throws NotFoundException {
        when(personServiceImpl.findById(anyLong())).thenThrow(
                new NotFoundException(String.format(PERSON_NOT_FOUND, ID)));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> addressServiceImpl.findAllPersonAddresses(ID, Pageable.ofSize(1)));

        assertEquals(String.format(PERSON_NOT_FOUND, ID), exception.getMessage());
    }

    @Test
    void whenUpdateFieldIsMainAddressReturnSuccess() throws NotFoundException, BadRequestException {
        when(personServiceImpl.findById(Mockito.any())).thenReturn(person);
        when(addressRepository.findByIdAndPerson(Mockito.any(), Mockito.any())).thenReturn(Optional.of(address));
        when(addressRepository.findByPersonAndIsMainTrue(Mockito.any())).thenReturn(Optional.empty());

        addressServiceImpl.updateFieldIsMainAddress(ID, ID, addressIsMainRequestDto);

        verify(addressRepository, times(1)).save(Mockito.any());
    }

    @Test
    void whenTryUpdateFieldIsMainAddressReturnNotFoundException() throws NotFoundException {
        when(personServiceImpl.findById(anyLong())).thenThrow(
                new NotFoundException(String.format(PERSON_NOT_FOUND, ID)));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> addressServiceImpl.updateFieldIsMainAddress(ID, ID, addressIsMainRequestDto));

        assertEquals(String.format(PERSON_NOT_FOUND, ID), exception.getMessage());
    }

    @Test
    void whenTryUpdateFieldIsMainAddressReturnBadRequestException() throws NotFoundException {
        when(personServiceImpl.findById(Mockito.any())).thenReturn(person);
        when(addressRepository.findByIdAndPerson(Mockito.any(), Mockito.any())).thenReturn(Optional.of(address));
        when(addressRepository.findByPersonAndIsMainTrue(Mockito.any())).thenReturn(Optional.of(address));
        address.setId(2l);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> addressServiceImpl.updateFieldIsMainAddress(ID, ID, addressIsMainRequestDto));

        assertEquals(THERE_IS_ALREADY_A_MAIN_ADDRESS, exception.getMessage());
    }
}