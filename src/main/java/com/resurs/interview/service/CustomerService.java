package com.resurs.interview.service;

import com.resurs.interview.db.CustomerEntity;
import com.resurs.interview.db.CustomerRepository;
import com.resurs.interview.gateway.TaxCheckGateway;
import com.resurs.interview.model.api.CustomerCreateRequest;
import com.resurs.interview.model.api.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TaxCheckGateway taxCheckGateway;

    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse createCustomer(CustomerCreateRequest customer) {
        CustomerEntity customerEntity = customerRepository.save(CustomerEntity.transform(customer));
        return CustomerResponse.transform(customerEntity);
    }

    public Optional<CustomerResponse> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).map(CustomerResponse::transform);
    }

    public Optional<Double> getCreditScore(Long customerId) {
        return customerRepository.findById(customerId).map(CustomerEntity::getCreditScore);
    }

    public boolean requestLoan(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    int lastYearTax = taxCheckGateway.getLastYearTax(customer.getSsn());
                    return shallWeGiveALoan(lastYearTax, customer.getCreditScore());
                }).orElse(false);

    }

    private boolean shallWeGiveALoan(int lastYearTax, Double creditScore) {
        return creditScore == 0.0 || creditScore > 450 && lastYearTax < 10000;
    }


    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerResponse::transform).toList();
    }
}
