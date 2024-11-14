package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.service.TransactionService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {
    private final TransactionService transactionService;

    @KafkaListener(topics = "t1_demo_transactions", groupId = "group_id")
    public void consume(CreateTransactionDto message) {
        transactionService.createTransaction(message);
        log.info("transaction created from dto: {}", message);
    }
}
