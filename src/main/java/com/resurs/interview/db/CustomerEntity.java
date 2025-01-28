package com.resurs.interview.db;

import com.resurs.interview.model.api.CustomerCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String postcode;
    private String city;
    @Column(name = "ssn", length = 13, nullable = false, unique = true)
    private String ssn; //socialSecurityNumber
    private Double creditScore;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;


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
                0.0,
                new ArrayList<>() {
                });
    }

}
