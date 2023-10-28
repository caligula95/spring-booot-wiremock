package com.wiremockexample.exception.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProblemResponse {

    private Integer status;
    @JsonProperty("Message")
    private String message;
}
