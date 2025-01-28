package com.resurs.interview.contoller;

import com.resurs.interview.db.TransactionEntity;
import com.resurs.interview.model.api.TransactionCreateRequest;
import com.resurs.interview.service.CustomerService;
import com.resurs.interview.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CustomerService customerService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionEntity>> getCustomerTransactions(@PathVariable Long customerId) {
        List<TransactionEntity> transactionsByCustomerId = transactionService.getTransactionsByCustomerId(customerId);
        if (transactionsByCustomerId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(transactionsByCustomerId);
    }

    @GetMapping("/customer/{customerId}/average")
    public ResponseEntity<Double> getAverageCustomerTransactions(@PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getAverageTransactionAmount(customerId));

    }

    @PostMapping("customer/{customerId}/create")
    public ResponseEntity<TransactionCreateRequest> createTransaction(@PathVariable Long customerId, @RequestBody TransactionCreateRequest transaction) {
        return customerService.getCustomerById(customerId)
                .map(c -> {
                    transactionService.createTransaction(customerId, transaction);
                    return new ResponseEntity<>(transaction, HttpStatus.CREATED);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }


}
