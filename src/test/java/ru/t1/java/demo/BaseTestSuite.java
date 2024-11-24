package ru.t1.java.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest
public abstract class BaseTestSuite {

    @Autowired
    protected MockMvc rest;

    @Autowired
    protected ObjectMapper objectMapper;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void init() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);

        stubFor(WireMock.get(WireMock.urlPathMatching("/account/status/1"))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("\"OPEN\"")));

        stubFor(WireMock.get(WireMock.urlEqualTo("/account/status/2"))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withBody("{\"status\": \"BLOCKED\"}")));
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

}
