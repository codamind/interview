package com.resurs.interview.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findBySsn(String ssn);

    @Override
    List<CustomerEntity> findAll();

    Optional<CustomerEntity> findById(Long customerId);
}
