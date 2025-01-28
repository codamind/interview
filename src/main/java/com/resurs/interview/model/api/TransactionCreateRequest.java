package com.resurs.interview.model.api;

import com.resurs.interview.model.db.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Data
public class TransactionCreateRequest {
    private double amount;
    private ZonedDateTime transactionDate;
    private TransactionType transactionType;
}
