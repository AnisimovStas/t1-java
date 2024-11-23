package ru.t1.java.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.client.SecondServiceClient;
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Account.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final SecondServiceClient secondServiceClient;

    @Override
    @Transactional
    public Transaction createTransaction(CreateTransactionDto dto) throws Exception {
        Transaction transaction = TransactionMapper.createTransaction(dto);
        AccountStatus status = secondServiceClient.getAccountStatus(dto.getAccountId());

        if (status == AccountStatus.BLOCKED) {
            accountService.suspiciousAction(dto.getAccountId(), dto.getAmount());
            transaction.setStatus(TransactionStatus.REJECTED);
        } else {
            accountService.updateBalance(dto.getAccountId(), dto.getAmount());
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateTransactionStatus(Transaction message) throws Exception {
        if (message == null) {
            throw new Exception("Transaction in message is null");
        }
        if (message.getId() == null) {
            throw new Exception("Transaction id in message is null");
        }

        Transaction transaction = transactionRepository.findById(message.getId()).orElse(null);
        if (transaction == null) {
            throw new Exception("Transaction not found in database");
        }

        switch (transaction.getStatus()) {
            case ACCECPTED -> acceptTransaction(transaction);
            case REJECTED -> rejectTransaction(transaction);
            case BLOCKED -> blockTransaction(transaction);
            default -> throw new Exception("Transaction status is not valid");
        }
    }

    private void blockTransaction(Transaction transaction) throws Exception {
        accountService.suspiciousAction(transaction.getAccountId(), transaction.getAmount());
        transaction.setStatus(TransactionStatus.BLOCKED);
        transactionRepository.save(transaction);
    }

    private void rejectTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.REJECTED);
        transactionRepository.save(transaction);
    }

    private void acceptTransaction(Transaction transaction) throws Exception {
        accountService.updateBalance(transaction.getAccountId(), transaction.getAmount());
        transaction.setStatus(TransactionStatus.ACCECPTED);
        transactionRepository.save(transaction);
    }
}
