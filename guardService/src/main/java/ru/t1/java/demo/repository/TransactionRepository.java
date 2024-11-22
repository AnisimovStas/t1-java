package ru.t1.java.demo.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Transaction;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryInternal {

}

interface TransactionRepositoryInternal {
    Long countRecentTransactions(Long accountId, Instant startTime);

    List<Transaction> findRecentTransactions(Long accountId, Instant startTime);
}

@Component
@RequiredArgsConstructor
@Slf4j
class TransactionRepositoryImpl implements TransactionRepositoryInternal {

    private final JdbcClient jdbcClient;

    private final RowMapper<Transaction> transactionRowMapper = new DataClassRowMapper<>(Transaction.class);

    @Override
    public Long countRecentTransactions(Long accountId, Instant startTime) {
        String sql = """
                SELECT COUNT(*)
                FROM transaction t
                WHERE t.account_id = :account_id
                  AND t.timestamp BETWEEN :start_time AND CURRENT_TIMESTAMP
            """;
        return jdbcClient.sql(sql)
            .param("account_id", accountId)
            .param("start_time", startTime)
            .query(Long.class)
            .single();
    }

    @Override
    public List<Transaction> findRecentTransactions(Long accountId, Instant startTime) {
        String sql = """
                SELECT *
                FROM transaction t
                WHERE t.account_id = :account_id
                  AND t.timestamp BETWEEN :start_time AND CURRENT_TIMESTAMP
            """;
        return jdbcClient.sql(sql)
            .param("account_id", accountId)
            .param("start_time", startTime)
            .query(transactionRowMapper)
            .list();
    }

}