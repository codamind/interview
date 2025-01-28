package com.resurs.interview.contoller;

import com.resurs.interview.model.api.CreditScoreResponse;
import com.resurs.interview.model.api.CustomerCreateRequest;
import com.resurs.interview.model.api.CustomerResponse;
import com.resurs.interview.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerCreateRequest customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomer() {
        List<CustomerResponse> allCustomers = customerService.getAllCustomers();
        if (allCustomers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/{customerId}/creditScore")
    public ResponseEntity<CreditScoreResponse> getCreditScore(@PathVariable Long customerId) {
        return customerService.getCreditScore(customerId)
                .map(score -> new ResponseEntity<>(new CreditScoreResponse(score), HttpStatus.OK))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/{customerId}/loan")
    public ResponseEntity<String> requestLoan(@PathVariable Long customerId) {

        boolean approved = customerService.requestLoan(customerId);
        return ResponseEntity.ok(approved ? "Loan approved" : "Loan denied");
    }
}
