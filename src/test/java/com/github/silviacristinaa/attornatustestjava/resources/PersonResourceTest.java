package com.github.silviacristinaa.attornatustestjava.resources;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.PersonResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.services.PersonService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PersonResourceTest {

    private static final long ID = 1l;
    private static final String NAME = "Test";
    private static final int INDEX = 0;

    private PersonRequestDto personRequestDto;
    private PersonResponseDto personResponseDto;

    private Person person;

    @InjectMocks
    private PersonResource personResource;

    @Mock
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRequestDto = new PersonRequestDto(NAME, LocalDate.now());

        personResponseDto = new PersonResponseDto(ID, NAME, LocalDate.now(), new ArrayList<>());

        person = new Person();
    }

    @Test
    void whenCreatePersonReturnCreated() {
        when(personService.create(Mockito.any())).thenReturn(person);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<Void> response = personResource.create(personRequestDto);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
    }

    @Test
    void whenUpdateReturnNoContent() throws NotFoundException {
        ResponseEntity<Void> response = personResource.update(ID, personRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
    }

    @Test
    void whenFindByIdReturnOnePersonResponseDto() throws NotFoundException {
        when(personService.findOnePersonById(anyLong())).thenReturn(personResponseDto);

        ResponseEntity<PersonResponseDto> response = personResource.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PersonResponseDto.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(LocalDate.now(), response.getBody().getBirthDate());
        assertEquals(ArrayList.class, response.getBody().getAddress().getClass());
    }

    @Test
    void whenFindAllReturnPersonResponseDtoPage() {
        when(personService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(personResponseDto)));

        ResponseEntity<Page<PersonResponseDto>> response = personResource.findAll(Pageable.unpaged());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PersonResponseDto.class, response.getBody().getContent().get(INDEX).getClass());

        assertEquals(ID, response.getBody().getContent().get(INDEX).getId());
        assertEquals(NAME, response.getBody().getContent().get(INDEX).getName());
        assertEquals(LocalDate.now(), response.getBody().getContent().get(INDEX).getBirthDate());
        assertEquals(ArrayList.class, response.getBody().getContent().get(INDEX).getAddress().getClass());
    }
}