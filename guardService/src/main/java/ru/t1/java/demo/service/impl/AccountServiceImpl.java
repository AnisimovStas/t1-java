package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.model.Account.AccountStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;
import ru.t1.java.demo.util.RandomUtils;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

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
        account.setStatus(AccountStatus.BLOCKED);
        account.setFrozenAmount(account.getFrozenAmount().add(amount));
        account.setBalance(account.getBalance().subtract(amount));
        this.updateAccount(account);
    }

    @Override
    public AccountStatus getAccountStatus(Long id) {
        Account account = this.getAccount(id);
        if (isRandomBanHummer()) {
            return AccountStatus.BLOCKED;
        }
        return account.getStatus();
    }

    private boolean isRandomBanHummer() {
        return RandomUtils.randomLong() % 2 == 0;
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
