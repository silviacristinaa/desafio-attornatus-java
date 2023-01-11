package com.github.silviacristinaa.attornatustestjava.services;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressIsMainRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.exceptions.BadRequestException;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

    Address create(Long personId, AddressRequestDto addressRequestDto) throws NotFoundException, BadRequestException;

    Page<AddressResponseDto> findAllPersonAddresses(Long personId, Pageable pageable) throws NotFoundException;

    void updateFieldIsMainAddress(Long id, Long addressId, AddressIsMainRequestDto addressIsMainRequestDto)
            throws NotFoundException, BadRequestException;
}