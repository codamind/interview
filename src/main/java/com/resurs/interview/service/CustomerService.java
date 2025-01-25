package com.resurs.interview.service;

import java.util.Optional;
import com.resurs.interview.db.CustomerEntity;
import com.resurs.interview.gateway.TaxCheckGateway;
import com.resurs.interview.model.api.CustomerCreateRequest;
import com.resurs.interview.model.api.CustomerResponse;
import com.resurs.interview.db.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TaxCheckGateway taxCheckGateway;

    public CustomerResponse createCustomer(CustomerCreateRequest customer) {
        CustomerEntity customerEntity = customerRepository.save(CustomerEntity.transform(customer));
        return CustomerResponse.transform(customerEntity);
    }

    public Optional<CustomerResponse> getCustomerById(Long customerId) {
      return customerRepository.findById(customerId).map(CustomerResponse::transform);
    }

    public Optional<Integer> getCreditScore(Long customerId) {
        return customerRepository.findById(customerId).map(CustomerEntity::getCreditScore);
    }

    public boolean requestLoan(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    int lastYearTax = taxCheckGateway.getLastYearTax(customer.getSsn());
                    // Call some advanced formula like "shallWeGiveALoan()"
                    return shallWeGiveALoan(lastYearTax, customer.getCreditScore());
                }).orElse(false);
    }

    private boolean shallWeGiveALoan(int lastYearTax, Integer creditScore) {
        return true;
        
    }
}
