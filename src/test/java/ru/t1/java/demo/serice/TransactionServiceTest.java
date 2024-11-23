package ru.t1.java.demo.serice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.t1.java.demo.client.SecondServiceClient;
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Account.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountService accountService;

    @MockBean
    private SecondServiceClient secondServiceClient;

    @Autowired
    private TransactionService transactionService;

    @Test
    void createTransactionTest() throws Exception {
        CreateTransactionDto dto = new CreateTransactionDto(1L, BigDecimal.valueOf(100));
        AccountStatus status = AccountStatus.OPEN;
        when(secondServiceClient.getAccountStatus(dto.getAccountId())).thenReturn(status);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(TransactionMapper.createTransaction(dto));

        Transaction result = transactionService.createTransaction(dto);

        Transaction expectedTx =
            Transaction.builder().status(TransactionStatus.REQUESTED)
                .build();

        assertNotNull(result);
        assertEquals(expectedTx.getStatus(), result.getStatus());
    }

    @Test
    void updateTransactionStatusTest() throws Exception {
        Transaction msg =
            Transaction.builder().transactionId(1L).amount(BigDecimal.ONE).status(TransactionStatus.REJECTED)
                .accountId(1L).build();

        when(transactionRepository.findByTransactionId(1L)).thenReturn(Optional.ofNullable(msg));

        transactionService.updateTransactionStatus(msg);
        verify(transactionRepository).save(msg);
    }

    @Test
    void updateTxWithIncorrectIdTest() throws Exception {
        Transaction msg =
            Transaction.builder().transactionId(null).build();

        assertThrows(Exception.class, () -> transactionService.updateTransactionStatus(msg), "Transaction id is null");
    }
}