package com.wiremockexample.service;


import com.wiremockexample.client.GithubClient;
import com.wiremockexample.client.response.GithubOwner;
import com.wiremockexample.client.response.GithubRepository;
import com.wiremockexample.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubRepositoryService implements RepositoryService {

    private final GithubClient githubClient;

    @Override
    public List<Repository> getFirstTenRepos(String username, boolean isFork) {
        int page = 1;
        int pageSize = 10;
        List<GithubRepository> githubResponse = githubClient.getRepositoriesByUsername(username, page, pageSize);

        return githubResponse.stream()
                .filter(it -> it.getFork().equals(isFork))
                .map(this::map)
                .collect(Collectors.toList());
    }

    private Repository map(GithubRepository githubRepository) {
        Repository repository = new Repository();
        repository.setName(githubRepository.getName());
        repository.setOwnerLogin(Optional.ofNullable(githubRepository.getOwner())
                .map(GithubOwner::getLogin)
                .orElse(null));
        return repository;
    }
}
