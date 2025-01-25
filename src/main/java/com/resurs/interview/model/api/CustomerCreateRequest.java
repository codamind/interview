package com.resurs.interview.model.api;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerCreateRequest {

    @NotBlank(message = "The firstName is required.")
    @Size(min = 3, max = 120, message = "Your firstName must be from 3 to 120 characters.")
    private final String firstName;
    @NotBlank(message = "The lastName is required.")
    @Size(min = 3, max = 120, message = "Your lastName must be from 3 to 120 characters.")
    private final String lastName;
    @NotEmpty(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
    private final String email;
    @NotEmpty(message = "Your address is required")
    private final String address;
    @Pattern(regexp = "^[0-9]{3}\\s?[0-9]{2}$", message = "A swedish postcode is required")
    private final String postcode;
    @NotEmpty
    private final String city;
    @NotNull
    @Pattern(regexp = "^\\d{6}(?:\\d{2})?[-\\s]?\\d{4}$") //RegEx for swedish social numbers
    private final String socialSecurityNumber;

}
