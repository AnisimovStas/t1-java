package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account createAccount(CreateAccountDto dto);

    Account getAccount(Long id);

    Account updateAccount(Account account);

    void updateBalance(Long accountId, BigDecimal amount) throws Exception;

    void suspiciousAction(Long accountId, BigDecimal amount) throws Exception;
}
