package ru.t1.java.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AccountConsumer {

    @KafkaListener(topics = "account-topic", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Account Consumer received: " + message);
    }
}
