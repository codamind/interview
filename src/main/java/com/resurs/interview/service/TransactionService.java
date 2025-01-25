package com.resurs.interview.service;

import java.util.List;
import com.resurs.interview.db.TransactionEntity;
import com.resurs.interview.db.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;

    /**
     * Get all transactions for a specific customer.
     * @param customerId the ID of the customer.
     * @return list of transactions for that customer.
     */
    public List<TransactionEntity> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    /**
     * Get the average transaction amount for the customer.
     * @param customerId the ID of the customer.
     * @return average transaction amount.
     */
    public double getAverageTransactionAmount(Long customerId) {
        List<TransactionEntity> transactions = getTransactionsByCustomerId(customerId);
        return transactions.stream()
                .mapToDouble(TransactionEntity::getAmount)
                .average()
                .orElse(0.0);
    }
}
