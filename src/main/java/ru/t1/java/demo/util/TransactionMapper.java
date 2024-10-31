package ru.t1.java.demo.util;

import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Transaction;

import java.time.Instant;

public class TransactionMapper {
    public static Transaction createTransaction(CreateTransactionDto dto) {
        return Transaction.builder()
            .accountId(dto.getAccountId())
            .amount(dto.getAmount())
            .timestamp(Instant.now())
            .build();
    }
}
