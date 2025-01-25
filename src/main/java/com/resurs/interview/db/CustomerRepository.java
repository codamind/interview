package com.resurs.interview.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findBySsn(String ssn);

    Optional<CustomerEntity> findById(Long customerId);
}
