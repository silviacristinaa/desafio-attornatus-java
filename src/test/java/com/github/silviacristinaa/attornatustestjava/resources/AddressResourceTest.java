package com.github.silviacristinaa.attornatustestjava.resources;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressIsMainRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.exceptions.BadRequestException;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.services.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AddressResourceTest {

    private static final long ID = 1l;
    private static final String PUBLIC_PLACE = "Test";
    private static final String ZIP_CODE = "8888888";
    private static final long NUMBER = 1l;
    private static final String CITY = "test";
    private static final int INDEX = 0;

    private AddressRequestDto addressRequestDto;
    private AddressIsMainRequestDto addressIsMainRequestDto;
    private AddressResponseDto addressResponseDto;

    private Address address;
    private Person person;

    @InjectMocks
    private AddressResource addressResource;

    @Mock
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressRequestDto = new AddressRequestDto(PUBLIC_PLACE, ZIP_CODE, NUMBER, CITY,
                true);
        addressIsMainRequestDto = new AddressIsMainRequestDto(true);
        addressResponseDto = new AddressResponseDto(ID, PUBLIC_PLACE, ZIP_CODE, NUMBER, CITY,
                true);

        person = new Person();
        address = new Address();
    }

    @Test
    void whenCreateAddressReturnCreated() throws NotFoundException, BadRequestException {
        when(addressService.create(Mockito.any(), Mockito.any())).thenReturn(address);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<Void> response = addressResource.create(ID, addressRequestDto);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
    }

    @Test
    void whenFindAllPersonAddressesReturnAddressResponseDtoPage() throws NotFoundException{
        when(addressService.findAllPersonAddresses(Mockito.any(), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(addressResponseDto)));

        ResponseEntity<Page<AddressResponseDto>> response = addressResource.findAllPersonAddresses
                (ID, Pageable.unpaged());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(AddressResponseDto.class, response.getBody().getContent().get(INDEX).getClass());

        assertEquals(ID, response.getBody().getContent().get(INDEX).getId());
        assertEquals(PUBLIC_PLACE, response.getBody().getContent().get(INDEX).getPublicPlace());
        assertEquals(ZIP_CODE, response.getBody().getContent().get(INDEX).getZipCode());
        assertEquals(NUMBER, response.getBody().getContent().get(INDEX).getNumber());
        assertEquals(CITY, response.getBody().getContent().get(INDEX).getCity());
        assertEquals(true, response.getBody().getContent().get(INDEX).isMain());
    }

    @Test
    void whenUpdateFieldIsMainAddressReturnNoContent() throws NotFoundException, BadRequestException {
        ResponseEntity<Void> response = addressResource.updateFieldIsMainAddress(ID, ID, addressIsMainRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
    }
}