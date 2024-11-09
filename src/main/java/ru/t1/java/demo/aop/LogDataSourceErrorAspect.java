package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.util.Topics;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceErrorAspect {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;
    private final KafkaTemplate kafkaTemplate;

    @Pointcut("within(ru.t1.java.demo.*)")
    public void loggingMethods() {
    }

    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)", throwing = "exception")
    public void logExceptionAnnotation(JoinPoint joinPoint, Throwable exception) {
        DataSourceErrorLog dataSourceErrorLog = DataSourceErrorLog.builder()
            .signature(joinPoint.getSignature().getName())
            .stacktrace(Arrays.toString(exception.getStackTrace()))
            .message(exception.getMessage())
            .build();

        //TODO вынести в kafkaMetricsProducer producer
        try {
            ProducerRecord<String, DataSourceErrorLog> message =
                new ProducerRecord<>(Topics.METRICS, dataSourceErrorLog);
            message.headers().add("ERROR", "DATA_SOURCE".getBytes());
            
            kafkaTemplate.send(message);
        } catch (Exception ex) {
            log.error("Ошибка при отправке сообщения в Kafka: {}", ex.getMessage());
            dataSourceErrorLogRepository.save(dataSourceErrorLog);
        } finally {
            kafkaTemplate.flush();
        }
    }

}
