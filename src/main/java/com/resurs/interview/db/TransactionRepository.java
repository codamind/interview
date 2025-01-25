package com.resurs.interview.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

    // Find transactions by customer ID
    List<TransactionEntity> findByCustomerId(Long customerId);
}