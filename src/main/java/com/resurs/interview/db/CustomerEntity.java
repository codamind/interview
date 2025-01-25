package com.resurs.interview.db;

import com.resurs.interview.model.api.CustomerCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String postcode;
    private String city;
    @Column(name = "ssn", length = 12, nullable = false, unique = true)
    private String ssn; //socialSecurityNumber
    private Integer creditScore;


    public static CustomerEntity transform(CustomerCreateRequest customer) {
        return new CustomerEntity(
                null,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPostcode(),
                customer.getCity(),
                customer.getSocialSecurityNumber(),
                null);
    }

}
