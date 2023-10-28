package com.wiremockexample.controller;

import com.wiremockexample.model.Repository;
import com.wiremockexample.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repo")
public class RepoController {

    private final RepositoryService repositoryService;

    @GetMapping
    public List<Repository> findFirstTenNonFork(@RequestParam String username) {
        return repositoryService.getFirstTenRepos(username, false);
    }
}
