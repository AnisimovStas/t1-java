package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.model.Account.AccountStatus;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Value("${t1.limits.rejected-transactions}")
    private int maxRejectedTransactionsLimit;

    @Override
    public Account createAccount(CreateAccountDto dto) {
        Account account = AccountMapper.createAccount(dto);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void updateBalance(Long accountId, BigDecimal amount) throws Exception {
        Account account = this.getAccount(accountId);
        validateAccount(account);
        account.setBalance(account.getBalance().add(amount));
        this.updateAccount(account);
    }

    @Override
    public void suspiciousAction(Long accountId, BigDecimal amount) throws Exception {
        Account account = this.getAccount(accountId);
        if (account == null) {
            throw new Exception("Account not found");
        }

        if (isConfirmedJulik(accountId)) {
            arrestAccount(account);
        } else {
            regularBlockAccount(account, amount);
        }

    }

    private boolean isConfirmedJulik(Long accountId) {
        int rejectedTransactionCount =
            transactionRepository.countByAccountIdAndStatus(
                accountId,
                String.valueOf(TransactionStatus.REJECTED.ordinal())
            );
        return rejectedTransactionCount >= maxRejectedTransactionsLimit;
    }

    private void regularBlockAccount(Account account, BigDecimal freezeAmount) {
        account.setStatus(AccountStatus.BLOCKED);
        account.setFrozenAmount(account.getFrozenAmount().add(freezeAmount));
        account.setBalance(account.getBalance().subtract(freezeAmount));
        this.updateAccount(account);
    }

    private void arrestAccount(Account account) {
        account.setStatus(AccountStatus.ARRESTED);
        this.updateAccount(account);
    }

    private void validateAccount(Account account) throws Exception {
        if (account == null) {
            throw new Exception("Account not found");
        }
        if (account.getStatus() != AccountStatus.OPEN) {
            throw new Exception("Something wrong with account");
        }
    }
}
