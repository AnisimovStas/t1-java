package ru.t1.java.demo.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.t1.java.demo.model.Account.AccountStatus;

@Component
@RequiredArgsConstructor
public class SecondServiceClient {
    public static String BASE_URL = "http://localhost:8081";
    RestTemplate restTemplate = new RestTemplate();

    public AccountStatus getAccountStatus(Long id) throws Exception {
        String url = BASE_URL + "/account/status/" + id;
        try {
            return restTemplate.getForObject(url, AccountStatus.class);
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }

}
