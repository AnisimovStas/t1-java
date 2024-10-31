package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;

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
}
