package dev.coding.springboot.service;

import dev.coding.springboot.gateway.github.GithubRestGateway;
import dev.coding.springboot.gateway.github.domain.GithubUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class GithubService {

    private final GithubRestGateway githubRestGateway;

    public GithubService(GithubRestGateway githubRestGateway) {
        this.githubRestGateway = githubRestGateway;
    }

    public GithubUser[] getUsers() {
        return githubRestGateway.getUsers();
    }

    public GithubUser getUserByUsername(final String username) {
        return githubRestGateway.getUserByUsername(username);
    }
}
