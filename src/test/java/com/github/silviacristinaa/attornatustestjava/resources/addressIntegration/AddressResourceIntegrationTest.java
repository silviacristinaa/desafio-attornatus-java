package com.github.silviacristinaa.attornatustestjava.resources.addressIntegration;

import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.repositories.AddressRepository;
import com.github.silviacristinaa.attornatustestjava.repositories.PersonRepository;
import com.github.silviacristinaa.attornatustestjava.resources.integrations.IntegrationTests;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddressResourceIntegrationTest extends IntegrationTests {

    private static final String PERSON_NOT_FOUND = "Person %s not found";
    private static final String ADDRESS_NOT_FOUND = "Address %s not found";
    private static final String THERE_IS_ALREADY_A_MAIN_ADDRESS =
            "There is already a main address, it is only possible to have one";

    private static final long ID = 99l;

    private String addressId;
    private Person person;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @Order(1)
    public void whenTryCreateAddressWithInvalidFieldsReturnBadRequestException() throws Exception {
        mvc.perform(post("/people/{id}/address", ID).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressBadRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Arguments not valid")));
    }

    @Test
    @Order(2)
    public void whenTryCreateAddressReturnNotFoundException() throws Exception {
        mvc.perform(post("/people/{id}/address", ID).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressCreateSuccess())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Not found")))
                .andExpect(jsonPath("errors.[0]", is(String.format(PERSON_NOT_FOUND, ID))));
    }

    @Test
    @Order(3)
    public void whenCreateAddressReturnCreated() throws Exception {
        person = personRepository.save(new Person(null, "Test", LocalDate.now(), new ArrayList<>()));

        mvc.perform(post("/people/{id}/address", person.getId()).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressCreateSuccess())))
                .andExpect(status().isCreated())
                .andDo(i -> {
                    addressId = getIdByLocation(i.getResponse().getHeader("Location"));
                });
    }

    @Test
    @Order(4)
    public void whenTryCreateAddressWithIsMainTrueReturnBadRequestException() throws Exception {
        mvc.perform(post("/people/{id}/address", person.getId()).headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressCreateSuccess())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Bad request")))
                .andExpect(jsonPath("errors.[0]", is(THERE_IS_ALREADY_A_MAIN_ADDRESS)));
    }

    @Test
    @Order(5)
    public void whenFindAllPersonAddressesReturnSuccess() throws Exception {
        mvc.perform(get("/people/{id}/address", person.getId()).headers(mockHttpHeaders()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].publicPlace", is("Test")))
                .andExpect(jsonPath("content[0].zipCode", is("8888888")))
                .andExpect(jsonPath("content[0].number", is(1)))
                .andExpect(jsonPath("content[0].city", is("test")))
                .andExpect(jsonPath("content[0].main", is(true)));
    }

    @Test
    @Order(6)
    public void whenFindAllPersonAddressesWithPersonNotFoundReturnNotFoundException() throws Exception {
        mvc.perform(get("/people/{id}/address", ID).headers(mockHttpHeaders()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Not found")))
                .andExpect(jsonPath("errors.[0]", is(String.format(PERSON_NOT_FOUND, ID))));
    }

    @Test
    @Order(7)
    public void whenUpdateFieldIsMainAddressWithAddressNotFoundReturnNotFoundException() throws Exception {
        mvc.perform(patch("/people/{id}/address/{address-id}", person.getId(), ID)
                        .headers(mockHttpHeaders())
                .content(objectMapper.writeValueAsString(
                        AddressResourceIntegrationBody.addressUpdateFieldIsMain(false))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Not found")))
                .andExpect(jsonPath("errors.[0]", is(String.format(ADDRESS_NOT_FOUND, ID))));
    }

    @Test
    @Order(8)
    public void whenUpdateFieldIsMainAddressWithPersonNotFoundReturnNotFoundException() throws Exception {
        mvc.perform(patch("/people/{id}/address/{address-id}", ID, addressId)
                        .headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressUpdateFieldIsMain(false))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Not found")))
                .andExpect(jsonPath("errors.[0]", is(String.format(PERSON_NOT_FOUND, ID))));
    }

    @Test
    @Order(9)
    public void whenUpdateFieldIsMainAddressReturnNoContent() throws Exception {
        mvc.perform(patch("/people/{id}/address/{address-id}", person.getId(), addressId)
                        .headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressUpdateFieldIsMain(false))))
                .andExpect(status().isNoContent());

        Optional<Address> address = addressRepository.findById(Long.valueOf(addressId));
        assertTrue(address.isPresent());
        assertEquals(address.get().isMain(), false);
    }

    @Test
    @Order(10)
    public void whenUpdateFieldIsMainAddressWithIsMainTrueReturnBadRequestException() throws Exception {
        addressRepository.save(new Address(null, "Test", "8888888",
                1l, "test", person, true));

        mvc.perform(patch("/people/{id}/address/{address-id}", person.getId(), addressId)
                        .headers(mockHttpHeaders())
                        .content(objectMapper.writeValueAsString(
                                AddressResourceIntegrationBody.addressUpdateFieldIsMain(true))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Bad request")))
                .andExpect(jsonPath("errors.[0]", is(THERE_IS_ALREADY_A_MAIN_ADDRESS)));
    }
}