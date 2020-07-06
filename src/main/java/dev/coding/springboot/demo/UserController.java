package dev.coding.springboot.demo;

import dev.coding.springboot.demo.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class UserController {

    private static final String PATH_GET_USERS = "/users";
    private static final String PATH_GET_USERS_USERNAME = "/users/{username}";

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = PATH_GET_USERS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User[]> getUsers() {
        final Optional<User[]> users = userService.getUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ok(users.get());
    }

    @GetMapping(value = PATH_GET_USERS_USERNAME, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") final String username) {
        final Optional<User> user = userService.getUserByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ok(user.get());
    }

}
