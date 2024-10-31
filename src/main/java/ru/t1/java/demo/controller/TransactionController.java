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
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable("id") Long id) {
        return transactionService.getTransaction(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody CreateTransactionDto dto) {
        return transactionService.createTransaction(dto);
    }

    @LogDataSourceError
    @PostMapping("/with-exception")
    public Transaction createTransactionWithException() {
        CreateTransactionDto dto = CreateTransactionDto.builder()
            .accountId(777L)
            .amount(BigDecimal.valueOf(10.0))
            .build();
        return transactionService.createTransaction(dto);
    }
}
