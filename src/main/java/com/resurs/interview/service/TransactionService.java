package com.resurs.interview.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.resurs.interview.db.CustomerEntity;
import com.resurs.interview.db.CustomerRepository;
import com.resurs.interview.db.TransactionEntity;
import com.resurs.interview.db.TransactionRepository;
import com.resurs.interview.model.api.TransactionCreateRequest;
import com.resurs.interview.model.db.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    /**
     * Get all transactions for a specific customer.
     *
     * @param customerId the ID of the customer.
     * @return list of transactions for that customer.
     */
    public List<TransactionEntity> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomer_Id(customerId);
    }

    /**
     * Get the average transaction amount for the customer.
     *
     * @param customerId the ID of the customer.
     * @return average transaction amount.
     */
    public double getAverageTransactionAmount(Long customerId) {
        List<TransactionEntity> transactions = getTransactionsByCustomerId(customerId);
        return calculateAverageAmount(transactions);
    }


    @Transactional(rollbackFor = Exception.class)
    public void createTransaction(Long customerId, TransactionCreateRequest transaction) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerId);
        customerEntity.ifPresent(customer -> {
            List<TransactionEntity> transactions = customer.getTransactions();
            TransactionEntity transactionNew = TransactionEntity.transform(customer, transaction);
            transactionRepository.save(transactionNew);
            transactions.add(transactionNew);
            customer.setCreditScore(calculateScore(transactions));
            customerRepository.save(customer);
        });
    }

    private double calculateScore(List<TransactionEntity> transactions) {
        double averageTransactionAmount = calculateAverageAmount(transactions);
        int transactionCount = transactions.size();
        AtomicReference<Double> normalizedBalance = getNormalizedBalance(transactions);
        return (normalizedBalance.get() * 0.5) + (averageTransactionAmount * 0.3) + (transactionCount * 0.2);
    }

    private static AtomicReference<Double> getNormalizedBalance(List<TransactionEntity> transactions) {
        AtomicReference<Double> normalizedBalance = new AtomicReference<>(0.0);
        transactions.forEach(transaction -> {
            if (transaction.getTransactionType().equals(TransactionType.DEBIT)) {
                normalizedBalance.set(normalizedBalance.get() - transaction.getAmount());
            } else {
                normalizedBalance.set(normalizedBalance.get() + transaction.getAmount());
            }
        });
        return normalizedBalance;
    }

    private static double calculateAverageAmount(List<TransactionEntity> transactions) {
        return transactions.stream()
                .mapToDouble(TransactionEntity::getAmount)
                .average()
                .orElse(0.0);
    }
}
