package ru.t1.java.demo.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.Account.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto implements Serializable {
    private Long id;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("client_id")
    private Long clientId;
    @JsonProperty("account_Type")
    private AccountType accountType;
    private BigDecimal balance;
    @JsonProperty("frozen_amount")
    private BigDecimal frozenAmount;
}
