package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;

public interface AccountService {

    Account createAccount(CreateAccountDto dto);

    Account getAccount(Long id);

}
