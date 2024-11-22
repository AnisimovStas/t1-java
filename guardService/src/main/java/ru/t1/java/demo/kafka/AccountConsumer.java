package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.service.AccountService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountConsumer {
    private final AccountService accountService;

    @KafkaListener(topics = "t1_demo_accounts", groupId = "group_id")
    public void consume(CreateAccountDto message) {
        accountService.createAccount(message);
        log.info("account created from dto: {}", message);
    }
}
