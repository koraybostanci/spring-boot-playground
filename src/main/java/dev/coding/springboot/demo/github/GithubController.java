package dev.coding.springboot.demo.github;

import dev.coding.springboot.demo.github.domain.GithubUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class GithubController {

    private static final String PATH_GET_USERS = "/users";
    private static final String PATH_GET_USERS_USERNAME = "/users/{username}";

    private final GithubService githubService;

    public GithubController(final GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(value = PATH_GET_USERS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GithubUser[]> getUsers() {
        final Optional<GithubUser[]> users = githubService.getUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ok(users.get());
    }

    @GetMapping(value = PATH_GET_USERS_USERNAME, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GithubUser> getUserByUsername(@PathVariable("username") final String username) {
        final Optional<GithubUser> user = githubService.getUserByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ok(user.get());
    }

}
