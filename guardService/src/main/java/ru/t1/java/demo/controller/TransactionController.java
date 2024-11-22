package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate kafkaTemplate;

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable("id") Long id) {
        return transactionService.getTransaction(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody CreateTransactionDto dto) throws Exception {
        return transactionService.createTransaction(dto);
    }

    @LogDataSourceError
    @PostMapping("/with-exception")
    public Transaction createTransactionWithException() throws Exception {
        CreateTransactionDto dto = CreateTransactionDto.builder()
            .accountId(777L)
            .amount(BigDecimal.valueOf(10.0))
            .build();
        return transactionService.createTransaction(dto);
    }

    @PostMapping("/kafka")
    public ResponseEntity<String> sendMessageToKafka() {
        //прошу прощения за kafkaTemplate в контроллере:(
        CreateTransactionDto dto = CreateTransactionDto.builder()
            .accountId(3L)
            .amount(BigDecimal.valueOf(10.0))
            .build();
        try {
            kafkaTemplate.send("t1_demo_transactions", dto);
        } finally {
            kafkaTemplate.flush();
        }

        return ResponseEntity.ok("Message sent to Kafka");
    }
}
