package com.github.silviacristinaa.attornatustestjava.repositories;

import com.github.silviacristinaa.attornatustestjava.entities.Address;
import com.github.silviacristinaa.attornatustestjava.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByPersonAndIsMainTrue(Person person);

    Optional<Address> findByIdAndPerson(Long id, Person person);

    List<Address> findByPerson(Person person);
}