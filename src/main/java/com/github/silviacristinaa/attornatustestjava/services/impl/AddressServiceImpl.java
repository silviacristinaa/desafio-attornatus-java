package com.github.silviacristinaa.attornatustestjava.services.impl;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressIsMainRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import com.github.silviacristinaa.attornatustestjava.exceptions.BadRequestException;
import com.github.silviacristinaa.attornatustestjava.exceptions.NotFoundException;
import com.github.silviacristinaa.attornatustestjava.repositories.AddressRepository;
import com.github.silviacristinaa.attornatustestjava.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private static final String THERE_IS_ALREADY_A_MAIN_ADDRESS =
            "There is already a main address, it is only possible to have one";
    private static final String ADDRESS_NOT_FOUND = "Address %s not found";

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    private final PersonServiceImpl personServiceImpl;

    @Override
    @Transactional
    public Address create(Long personId, AddressRequestDto addressRequestDto) throws NotFoundException,
            BadRequestException {
        Person person = personServiceImpl.findById(personId);

        if (addressRequestDto.isMain() && findByPersonAndIsMainTrue(person).isPresent()) {
            throw new BadRequestException(THERE_IS_ALREADY_A_MAIN_ADDRESS);
        }

        Address address = modelMapper.map(addressRequestDto, Address.class);
        address.setPerson(person);

        return addressRepository.save(address);
    }

    @Override
    public Page<AddressResponseDto> findAllPersonAddresses(Long personId, Pageable pageable)
            throws NotFoundException {
        Person person = personServiceImpl.findById(personId);

        List<AddressResponseDto> response = addressRepository
                .findByPerson(person)
                .stream().map(address -> modelMapper.map(address, AddressResponseDto.class))
                .collect(Collectors.toList());

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), response.size());

        Page<AddressResponseDto> page = new PageImpl<>(response.subList(start, end), pageable, response.size());

        return page;
    }

    @Override
    @Transactional
    public void updateFieldIsMainAddress(Long personId, Long addressId,
                                         AddressIsMainRequestDto addressIsMainRequestDto)
            throws NotFoundException, BadRequestException {
        Person person = personServiceImpl.findById(personId);
        Address address = findByIdAndPerson(addressId, person);

        Optional<Address> addressMain = findByPersonAndIsMainTrue(person);

        if (addressIsMainRequestDto.isMain() && addressMain.isPresent()
                && addressMain.get().getId() != addressId){
            throw new BadRequestException(THERE_IS_ALREADY_A_MAIN_ADDRESS);
        }

        address.setMain(addressIsMainRequestDto.isMain());
        addressRepository.save(address);
    }

    private Optional<Address> findByPersonAndIsMainTrue(Person person) {
        return addressRepository.findByPersonAndIsMainTrue(person);
    }

    private Address findByIdAndPerson(Long id, Person person) throws NotFoundException {
        return addressRepository.findByIdAndPerson(id, person)
                .orElseThrow(() -> new NotFoundException(String.format(ADDRESS_NOT_FOUND, id)));
    }
}