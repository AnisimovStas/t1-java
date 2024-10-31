package ru.t1.java.demo.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.Account.AccountType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAccountDto implements Serializable {

    @JsonProperty("client_id")
    private Long clientId;
    @JsonProperty("account_type")
    private AccountType accountType;
}
