package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(CreateTransactionDto dto) throws Exception;

    Transaction getTransaction(Long id);

    void updateTransactionStatus(Transaction message) throws Exception;
}
