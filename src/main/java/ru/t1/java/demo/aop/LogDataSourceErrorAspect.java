package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceErrorAspect {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

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

        dataSourceErrorLogRepository.save(dataSourceErrorLog);
    }

}
