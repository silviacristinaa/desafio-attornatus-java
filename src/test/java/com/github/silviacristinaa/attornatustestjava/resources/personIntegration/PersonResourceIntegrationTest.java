package com.github.silviacristinaa.attornatustestjava.resources.personIntegration;

import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.repositories.PersonRepository;
import com.github.silviacristinaa.attornatustestjava.resources.integrations.IntegrationTests;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonResourceIntegrationTest extends IntegrationTests {

    private String personId;
    private LocalDate date;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Order(1)
    public void whenTryCreatePersonWithInvalidFieldsReturnBadRequestException() throws Exception {
        mvc.perform(post("/people").headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                PersonResourceIntegrationBody.personBadRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Arguments not valid")));
    }

    @Test
    @Order(2)
    public void whenCreatePersonReturnCreated() throws Exception {
        date = LocalDate.now();
        mvc.perform(post("/people").headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                PersonResourceIntegrationBody.personCreateSuccess(date))))
                .andExpect(status().isCreated())
                .andDo(i -> {
                    personId = getIdByLocation(i.getResponse().getHeader("Location"));
                });
    }

    @Test
    @Order(3)
    public void whenTryUpdatePersonWithInvalidFieldsReturnBadRequestException() throws Exception {
        mvc.perform(put("/people/{id}", personId).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                PersonResourceIntegrationBody.personBadRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Arguments not valid"));
    }

    @Test
    @Order(4)
    public void whenTryUpdateWithIncorrectIdReturnNotFound() throws Exception {
        mvc.perform(put("/people/{id}", 999).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                PersonResourceIntegrationBody.personCreateSuccess(date))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Not found")))
                .andExpect(jsonPath("errors.[0]", is("Person 999 not found")));
    }

    @Test
    @Order(5)
    public void whenUpdateWithValueExistsReturnNoContent() throws Exception {
        mvc.perform(put("/people/{id}", personId).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                PersonResourceIntegrationBody.personCreateSuccess(date))))
                .andExpect(status().isNoContent());

        Optional<Person> person = personRepository.findById(Long.valueOf(personId));
        assertTrue(person.isPresent());
        assertEquals(person.get().getName(), "Test");
        assertEquals(person.get().getBirthDate(), date);
    }

    @Test
    @Order(6)
    public void whenTryFindByIdWithIncorrectIdReturnNotFound() throws Exception {
        mvc.perform(get("/people/{id}", 999).headers(mockHttpHeaders()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Not found")))
                .andExpect(jsonPath("errors.[0]", is("Person 999 not found")));
    }

    @Test
    @Order(7)
    public void whenFindByIdWithCorrectIdReturnSuccess() throws Exception {
        mvc.perform(get("/people/{id}", personId).headers(mockHttpHeaders()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test")))
                .andExpect(jsonPath("birthDate", is(date.toString())));
    }

    @Test
    @Order(8)
    public void whenFindAllReturnSuccess() throws Exception {
        mvc.perform(get("/people").headers(mockHttpHeaders()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].name", is("Test")))
                .andExpect(jsonPath("content[0].birthDate", is(date.toString())));
    }
}