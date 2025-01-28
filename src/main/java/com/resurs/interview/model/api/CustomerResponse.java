package com.resurs.interview.model.api;

import com.resurs.interview.db.CustomerEntity;

public record CustomerResponse(Long id, String firstName, String lastName, String email, String address,
                               String postcode, String city, String socialSecurityNumber, Double creditScore) {

    public static CustomerResponse transform(CustomerEntity customerEntity) {
        return new CustomerResponse(
                customerEntity.getId(),
                customerEntity.getFirstName(),
                customerEntity.getLastName(),
                customerEntity.getEmail(),
                customerEntity.getAddress(),
                customerEntity.getPostcode(),
                customerEntity.getCity(),
                customerEntity.getSsn(),
                customerEntity.getCreditScore()
        );
    }
}
