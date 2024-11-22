package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.ExecutionTimeMessageDto;
import ru.t1.java.demo.util.MessageHeader;
import ru.t1.java.demo.util.Topics;

import java.util.Arrays;

@Async
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    public static final long MAX_EXECUTION_TIME = 500;
    private final KafkaTemplate kafkaTemplate;

    @Pointcut("within(ru.t1.java.demo.*)")
    public void metricMethods() {
    }

    @Around("@annotation(ru.t1.java.demo.aop.Metric)")
    public void measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Считаем время выполнения для: {}", joinPoint.getSignature().getName());
        long startTime = System.currentTimeMillis();

        joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if (executionTime > MAX_EXECUTION_TIME) {
            sendMetricMessage(joinPoint, executionTime);
        }
    }

    private void sendMetricMessage(ProceedingJoinPoint joinPoint, long executionTime) {
        ExecutionTimeMessageDto executionTimeMessageDto = ExecutionTimeMessageDto.builder()
            .executionTime(executionTime)
            .method(joinPoint.getSignature().getName())
            .methodParams(Arrays.toString(joinPoint.getArgs()))
            .build();

        ProducerRecord<String, ExecutionTimeMessageDto> message =
            new ProducerRecord<>(Topics.EXECUTION_TIME, executionTimeMessageDto);
        message.headers().add(MessageHeader.ERROR, "METRICS".getBytes());

        try {
            kafkaTemplate.send(message);
        } finally {
            kafkaTemplate.flush();
        }
    }
}
