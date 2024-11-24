package ru.t1.java.demo.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Transaction;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryInternal {
    Optional<Transaction> findByTransactionId(Long transactionId);
}

interface TransactionRepositoryInternal {
    int countByAccountIdAndStatus(Long accountId, String transactionStatus);
}

@Component
@RequiredArgsConstructor
@Slf4j
class TransactionRepositoryImpl implements TransactionRepositoryInternal {
    private final JdbcClient jdbcClient;

    @Override
    public int countByAccountIdAndStatus(Long accountId, String transactionStatus) {
        return jdbcClient.sql(
                """
                       SELECT COUNT(*)
                       FROM transaction t
                       WHERE t.account_id = :account_id
                         AND t.status = :transaction_status
                    """
            ).param("account_id", accountId)
            .param("transaction_status", transactionStatus)
            .query(Integer.class)
            .single();
    }
}
