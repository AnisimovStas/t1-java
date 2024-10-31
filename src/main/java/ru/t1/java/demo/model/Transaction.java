package ru.t1.java.demo.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction extends AbstractPersistable<Long> {

    private Long accountId;

    private BigDecimal amount;

    private Date date;
}
