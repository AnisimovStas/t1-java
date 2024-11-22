package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {
    private final TransactionService transactionService;

    @KafkaListener(topics = "${t1.topic.transaction-accept}", groupId = "group_id")
    public void validateTransactionHandler(Transaction message) {
        try {
            transactionService.validateTransaction(message);
        } catch (Exception e) {
            log.error("woops", e.getLocalizedMessage());
        }
        log.info("transaction created from dto: {}", message);
    }
}
