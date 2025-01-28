package com.resurs.interview.model.api;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerCreateRequestTest {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Test
    void testCustomerValidationFailed() {
        CustomerCreateRequest t = new CustomerCreateRequest("t", "t", "NotAnEmail@ddd", "Address", "2334333", "TestCity", "345345");
        Set<ConstraintViolation<CustomerCreateRequest>> validate = factory.getValidator().validate(t);
        List<String> validationErrors = validate.stream().map(ConstraintViolation::getMessage).toList();

        assertTrue(validationErrors.contains("Your firstName must be from 2 to 120 characters."));
        assertTrue(validationErrors.contains("Your lastName must be from 2 to 120 characters."));
        assertTrue(validationErrors.contains("The email is not a valid email."));
        assertTrue(validationErrors.contains("A swedish postcode is required"));
        assertTrue(validationErrors.contains("This is not a swedish social number."));
    }

    @Test
    void testCustomerValidationSuccessful() {
        CustomerCreateRequest t = new CustomerCreateRequest("Thor", "Lambert", "test@test.com", "Address 23", "110 11", "TestCity", "640902-4624");
        Set<ConstraintViolation<CustomerCreateRequest>> validate = factory.getValidator().validate(t);
        assertTrue(validate.isEmpty());
    }
}