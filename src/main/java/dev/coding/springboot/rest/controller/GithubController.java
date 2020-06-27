package dev.coding.springboot.rest.controller;

import dev.coding.springboot.gateway.github.GithubRestGateway;
import dev.coding.springboot.gateway.github.domain.GithubUser;
import dev.coding.springboot.service.GithubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

    private final GithubService githubService;

    public GithubController(final GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(value = "/users")
    public GithubUser[] getUsers() {
        final GithubUser[] users = githubService.getUsers();
        return users;
    }

    @GetMapping(value = "/users/{username}")
    public GithubUser getUserByUsername(@PathVariable("username") final String username) {
        final GithubUser user = githubService.getUserByUsername(username);
        return user;
    }

}
