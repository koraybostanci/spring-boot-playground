package dev.coding.springboot.demo;

import dev.coding.springboot.demo.domain.User;
import dev.coding.springboot.demo.github.GithubGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final GithubGateway githubGateway;

    public UserService(GithubGateway githubGateway) {
        this.githubGateway = githubGateway;
    }

    public Optional<User[]> getUsers() {
        return githubGateway.getUsers();
    }

    public Optional<User> getUserByUsername(final String username) {
        return githubGateway.getUserByUsername(username);
    }

}
