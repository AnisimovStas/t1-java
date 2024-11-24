package ru.t1.java.demo.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.t1.java.demo.BaseTestSuite;
import ru.t1.java.demo.dto.CreateTransactionDto;
import ru.t1.java.demo.model.Account.Account;
import ru.t1.java.demo.model.Account.AccountStatus;
import ru.t1.java.demo.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends BaseTestSuite {
    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    public void initLocal(
        WebApplicationContext webApplicationContext
    ) {
        this.rest = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    @SneakyThrows
    void createTransactionTest() {
        CreateTransactionDto dto = CreateTransactionDto.builder()
            .accountId(1L).amount(BigDecimal.ONE).build();

        Account account = Account.builder().balance(BigDecimal.ONE).status(AccountStatus.OPEN).accountId(1L).build();

        when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(account));

        rest.perform(
                post("/transaction")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value("REQUESTED"));
    }
}
