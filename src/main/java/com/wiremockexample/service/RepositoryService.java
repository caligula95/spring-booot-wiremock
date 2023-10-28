package com.wiremockexample.service;


import com.wiremockexample.model.Repository;

import java.util.List;

public interface RepositoryService {

    List<Repository> getFirstTenRepos(String username, boolean isFork);
}
