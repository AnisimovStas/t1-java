package ru.t1.java.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    @Value("${t1.transaction.max-frequency.interval}")
    private Long maxFrequencyInterval;

    @Value("${t1.transaction.max-frequency.count}")
    private Long maxFrequencyCount;

    private final KafkaTemplate kafkaTemplate;

    @Value("${t1.topic.transaction-result}")
    private String txResultTopic;

    @Override
    @Transactional
    public Transaction createTransaction(CreateTransactionDto dto) throws Exception {
        accountService.updateBalance(dto.getAccountId(), dto.getAmount());
        return transactionRepository.save(TransactionMapper.createTransaction(dto));
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

    @Override
    public void validateTransaction(Transaction message) {
        if (isTooFrequency(message)) {
            return;
        }
        if (isOverdraft(message)) {
            return;
        }

        try {
            kafkaTemplate.send(txResultTopic, message);
        } finally {
            kafkaTemplate.flush();
        }

    }

    private Boolean isTooFrequency(Transaction tx) {
        Instant startTime = Instant.now().minus(Duration.ofMillis(maxFrequencyInterval));
        Long recentTransactionCount = transactionRepository.countRecentTransactions(tx.getAccountId(), startTime);
        if (recentTransactionCount >= maxFrequencyCount) {
            List<Transaction> transactions =
                transactionRepository.findRecentTransactions(tx.getAccountId(), startTime);
            transactions.forEach(trx -> {
                trx.setStatus(TransactionStatus.BLOCKED);
                try {
                    // сохранять и распоряжаться судьбой транзакций будет уже 1ый сервис
                    kafkaTemplate.send(txResultTopic, trx);
                } finally {
                    kafkaTemplate.flush();
                }
            });
            return true;
        }
        return false;
    }

    private Boolean isOverdraft(Transaction tx) {
        Account account = accountService.getAccount(tx.getAccountId());

        if (account.getBalance().compareTo(tx.getAmount()) < 0) {
            tx.setStatus(TransactionStatus.REJECTED);
            try {
                kafkaTemplate.send(txResultTopic, tx);
            } finally {
                kafkaTemplate.flush();
            }
            return true;
        }
        return false;
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
