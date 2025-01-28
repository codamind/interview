package com.resurs.interview.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resurs.interview.model.api.TransactionCreateRequest;
import com.resurs.interview.model.db.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Long id;
    private double amount;
    private Instant transactionDate;
    private TransactionType transactionType;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    public static TransactionEntity transform(CustomerEntity customer, TransactionCreateRequest transaction) {
        return new TransactionEntity(null, transaction.getAmount(), transaction.getTransactionDate().toInstant(), transaction.getTransactionType(), customer);
    }
}
