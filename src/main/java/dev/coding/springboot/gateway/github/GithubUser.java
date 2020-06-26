package dev.coding.springboot.gateway.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GithubUser implements Serializable {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "login")
    private String login;

    @JsonProperty(value = "url")
    private String url;
}
