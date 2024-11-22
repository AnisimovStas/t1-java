package ru.t1.java.demo.util;

import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;

import java.time.Instant;

public class TransactionMapper {
    public static Transaction createTransaction(CreateTransactionDto dto) {
        return Transaction.builder()
            .transactionId(RandomUtils.randomLong())
            .status(TransactionStatus.REQUESTED)
            .accountId(dto.getAccountId())
            .amount(dto.getAmount())
            .timestamp(Instant.now())
            .build();
    }
}
