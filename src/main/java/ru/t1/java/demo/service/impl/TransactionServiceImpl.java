package ru.t1.java.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.model.Account.AccountStatus;
import ru.t1.java.demo.model.Transaction;
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

    @Override
    @Transactional
    public Transaction createTransaction(CreateTransactionDto dto) throws Exception {

        Account account = accountService.getAccount(dto.getAccountId());
        validateAccount(account);
        account.setBalance(account.getBalance().add(dto.getAmount()));
        accountService.updateAccount(account);

        return transactionRepository.save(TransactionMapper.createTransaction(dto));
    }

    private void validateAccount(Account account) throws Exception {
        if (account == null) {
            throw new Exception("Account not found");
        }
        if (account.getStatus() != AccountStatus.OPEN) {
            throw new Exception("Something wrong with account");
        }
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
