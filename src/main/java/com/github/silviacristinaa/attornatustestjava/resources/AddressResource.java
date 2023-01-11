package com.github.silviacristinaa.attornatustestjava.resources;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressIsMainRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.PersonRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.PersonResponseDto;
import com.github.silviacristinaa.attornatustestjava.exceptions.BadRequestException;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.services.AddressService;
import com.github.silviacristinaa.attornatustestjava.services.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/people/{id}/address")
@RequiredArgsConstructor
public class AddressResource {

    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Void> create(@PathVariable("id") Long personId, @RequestBody @Valid AddressRequestDto addressRequestDto)
            throws NotFoundException, BadRequestException {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(addressService.create(personId,
                        addressRequestDto).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Page<AddressResponseDto>> findAllPersonAddresses(@PathVariable("id") Long personId,
                                                                           Pageable pageable)
            throws NotFoundException{
        return ResponseEntity.ok(addressService.findAllPersonAddresses(personId, pageable));
    }

    @PatchMapping("/{address-id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateFieldIsMainAddress(@PathVariable("id") Long personId,
                                                         @PathVariable("address-id") Long addressId,
                                                         @RequestBody AddressIsMainRequestDto addressIsMainRequestDto)
            throws NotFoundException, BadRequestException {
        addressService.updateFieldIsMainAddress(personId, addressId, addressIsMainRequestDto);
        return ResponseEntity.noContent().build();
    }
}