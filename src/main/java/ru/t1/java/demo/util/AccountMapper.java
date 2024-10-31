package ru.t1.java.demo.util;

import ru.t1.java.demo.dto.account.AccountDto;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;

import java.math.BigDecimal;

public class AccountMapper {

    public static Account createAccount(CreateAccountDto accountDto) {
        return Account.builder()
            .clientId(accountDto.getClientId())
            .accountType(accountDto.getAccountType())
            .balance(BigDecimal.valueOf(0))
            .build();
    }

    public static Account toEntity(AccountDto dto) {

        return Account.builder()
            .clientId(dto.getClientId())
            .accountType(dto.getAccountType())
            .balance(dto.getBalance())
            .build();
    }

    public static AccountDto toDto(Account entity) {
        return AccountDto.builder()
            .id(entity.getId())
            .clientId(entity.getClientId())
            .accountType(entity.getAccountType())
            .balance(entity.getBalance())
            .build();
    }

}
