package dev.coding.springboot.demo.github;

import dev.coding.springboot.demo.github.domain.GithubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static dev.coding.springboot.demo.github.common.TestObjectFactory.*;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class GithubControllerTest {

    @Mock
    private GithubService githubService;

    private GithubController githubController;

    @BeforeEach
    public void setUp() {
        githubController = new GithubController(githubService);
    }

    @Test
    public void getUserByUsername_returnsUser_whenUserExistsWithGivenUsername() {
        final GithubUser userToRetrieve = anyGithubUser(ANY_GITHUB_USER_ID, ANY_GITHUB_USER_NAME);

        when(githubService.getUserByUsername(ANY_GITHUB_USER_NAME)).thenReturn(Optional.of(userToRetrieve));

        final ResponseEntity<GithubUser> retrievedUser = githubController.getUserByUsername(ANY_GITHUB_USER_NAME);

        assertThat(retrievedUser.getStatusCode()).isEqualTo(OK);
        assertThat(retrievedUser.getBody()).isEqualTo(userToRetrieve);
    }

    @Test
    public void getUserByUsername_doesNotReturnAnyUser_whenUserDoesNotExistWithGivenUsername() {
        when(githubService.getUserByUsername(ANY_GITHUB_USER_NAME)).thenReturn(empty());

        final ResponseEntity<GithubUser> retrievedUser = githubController.getUserByUsername(ANY_GITHUB_USER_NAME);

        assertThat(retrievedUser.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(retrievedUser.getBody()).isNull();
    }

    @Test
    public void getUsers_returnsUsers_whenUsersExist() {
        final GithubUser[] usersToRetrieve = anyGithubUserList(3);

        when(githubService.getUsers()).thenReturn(Optional.of(usersToRetrieve));

        final ResponseEntity<GithubUser[]> retrievedUsers = githubController.getUsers();

        assertThat(retrievedUsers.getStatusCode()).isEqualTo(OK);
        assertThat(retrievedUsers.getBody()).isEqualTo(usersToRetrieve);
    }

    @Test
    public void getUsers_doesNotReturnUsers_whenUsersDoNotExist() {
        when(githubService.getUsers()).thenReturn(empty());

        final ResponseEntity<GithubUser[]> retrievedUsers = githubController.getUsers();

        assertThat(retrievedUsers.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(retrievedUsers.getBody()).isNull();
    }

}
