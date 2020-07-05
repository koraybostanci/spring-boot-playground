package dev.coding.springboot.demo.github;

import dev.coding.springboot.demo.github.domain.GithubUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GithubService {

    private final GithubGateway githubGateway;

    public GithubService(GithubGateway githubGateway) {
        this.githubGateway = githubGateway;
    }

    public Optional<GithubUser[]> getUsers() {
        return githubGateway.getUsers();
    }

    public Optional<GithubUser> getUserByUsername(final String username) {
        return githubGateway.getUserByUsername(username);
    }
}
