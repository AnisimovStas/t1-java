package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.model.Account.AccountType;
import ru.t1.java.demo.service.AccountService;

import static ru.t1.java.demo.aop.MetricAspect.MAX_EXECUTION_TIME;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable("id") Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping
    public Account createAccount(@RequestBody CreateAccountDto dto) {
        return accountService.createAccount(dto);
    }

    @LogDataSourceError
    @PostMapping("/with-exception")
    public Account createAccountWithException() {
        CreateAccountDto dto = CreateAccountDto.builder().clientId(777L).accountType(AccountType.CREDIT).build();
        return accountService.createAccount(dto);
    }

    @Metric
    @GetMapping("/with-delay")
    public Account getAccountWithDelay() throws InterruptedException {
        Thread.sleep(MAX_EXECUTION_TIME * 2);
        return accountService.getAccount(1L);
    }
}
