package com.wiremockexample.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WireMockTest(httpPort = 8181)
@AutoConfigureMockMvc
public class RepoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetUserRepos() throws Exception {
        mockMvc.perform(get("/repo?username=nonForkOneRepo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", equalTo("bort")))
                .andExpect(jsonPath("$.[0].ownerLogin", equalTo("abcd")));
    }

    @Test
    void shouldReturnErrorIfClientException() throws Exception {

        stubFor(WireMock.post(urlPathMatching("/users/clientExceptionUser/repos/?([a-z]*)"))
                .willReturn(status(403)
                        .withHeader("Content-Type", "application/json")
                        ));

        mockMvc.perform(get("/repo?username=clientExceptionUser")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0)));
    }
}
