package dev.coding.springboot.demo.github.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GithubUser implements Serializable {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "login")
    private String login;

    @JsonProperty(value = "url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
