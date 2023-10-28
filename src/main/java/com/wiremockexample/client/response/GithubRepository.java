package com.wiremockexample.client.response;

import lombok.Data;

@Data
public class GithubRepository {

    private String name;
    private GithubOwner owner;
    private Boolean fork;
}
