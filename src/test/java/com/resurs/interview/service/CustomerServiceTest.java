package com.resurs.interview.service;

import com.resurs.interview.db.CustomerEntity;
import com.resurs.interview.db.CustomerRepository;
import com.resurs.interview.gateway.TaxCheckGateway;
import com.resurs.interview.model.api.CustomerCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TaxCheckGateway taxCheckGateway;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, taxCheckGateway);
    }

    @Test
    void createCustomer() {
        //given
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("Thor", "Lambert", "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624");
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(new CustomerEntity());
        //when
        customerService.createCustomer(customerCreateRequest);
        //then
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));

    }

    @Test
    void requestLoan_customerNotFound_loanDenied() {
        //given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(null);
        //when
        boolean result = customerService.requestLoan(customerId);
        //then
        assertFalse(result);
    }

    @Test
    void requestLoan_customerFoundButNotLiable_loanDenied() {
        //given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new CustomerEntity(customerId, "Thor", "Lambert",
                "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624", 100.00, new ArrayList<>())));
        //when
        boolean result = customerService.requestLoan(customerId);
        //then
        assertFalse(result);
    }

    @Test
    void requestLoan_customerFoundButLiable_loanGranted() {
        //given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new CustomerEntity(customerId, "Thor", "Lambert",
                "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624", 600.00, new ArrayList<>())));
        //when
        boolean result = customerService.requestLoan(customerId);
        //then
        assertTrue(result);
    }


}