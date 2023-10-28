package com.wiremockexample.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.wiremockexample.client.response.GithubOwner;
import com.wiremockexample.client.response.GithubRepository;
import com.wiremockexample.model.Repository;
import com.wiremockexample.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.wiremockexample.client.GithubClient.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@WireMockTest(httpPort = 8181)
public class RepositoryServiceIntegrationTest {

    @Autowired
    private RepositoryService repositoryService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getFirstTenNonForkReposReturnsFirstTenNonForkRepos(WireMockRuntimeInfo wireMockRuntimeInfo) {

        GithubRepository githubRepository = new GithubRepository();
        githubRepository.setName("abc");
        githubRepository.setFork(false);
        GithubOwner githubOwner = new GithubOwner();
        githubOwner.setLogin("OwnerLogin");
        githubRepository.setOwner(githubOwner);

        GithubRepository githubRepository1 = new GithubRepository();
        githubRepository1.setName("cbd");
        githubRepository1.setFork(true);
        githubRepository1.setOwner(githubOwner);

        List<GithubRepository> repos = new ArrayList<>();
        repos.add(githubRepository1);
        repos.add(githubRepository);

        JsonNode jsonNode = objectMapper.valueToTree(repos);

        stubFor(get(urlPathMatching("/users/caligula95/repos/?([a-z]*)"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(jsonNode)));

        List<Repository> response = repositoryService.getFirstTenRepos("caligula95", false);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);

        verify(getRequestedFor(urlPathMatching("/users/caligula95/repos/?([a-z]*)"))
                .withQueryParam(TYPE, equalTo(SOURCE))
                .withQueryParam(PAGE, equalTo("1"))
                .withQueryParam(PER_PAGE, equalTo("10")));

    }

    @Test
    void getFirstTenForkReposReturnsFirstTenForkRepos() {

        List<Repository> response = repositoryService.getFirstTenRepos("abcd", true);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);

        verify(getRequestedFor(urlPathMatching("/users/abcd/repos/?([a-z]*)"))
                .withQueryParam(TYPE, equalTo(SOURCE))
                .withQueryParam(PAGE, equalTo("1"))
                .withQueryParam(PER_PAGE, equalTo("10")));

    }

    @Test
    void getFirstTenForkReposTimeoutError() {

        stubFor(get(urlPathMatching("/users/timeoutCaseUser/repos/?([a-z]*)"))
                .willReturn(ok()
                                .withFixedDelay(5000)
                        .withHeader("Content-Type", "application/json")
                        ));

        List<Repository> response = repositoryService.getFirstTenRepos("timeoutCaseUser", true);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(0);

        verify(getRequestedFor(urlPathMatching("/users/timeoutCaseUser/repos/?([a-z]*)"))
                .withQueryParam(TYPE, equalTo(SOURCE))
                .withQueryParam(PAGE, equalTo("1"))
                .withQueryParam(PER_PAGE, equalTo("10")));

    }
}
