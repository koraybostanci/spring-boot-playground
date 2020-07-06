package dev.coding.springboot.demo;

import dev.coding.springboot.demo.common.TestObjectFactory;
import dev.coding.springboot.demo.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    public void getUserByUsername_returnsUser_whenUserExistsWithGivenUsername() {
        final User userToRetrieve = TestObjectFactory.anyGithubUser(TestObjectFactory.ANY_GITHUB_USER_ID, TestObjectFactory.ANY_GITHUB_USER_NAME);

        when(userService.getUserByUsername(TestObjectFactory.ANY_GITHUB_USER_NAME)).thenReturn(Optional.of(userToRetrieve));

        final ResponseEntity<User> retrievedUser = userController.getUserByUsername(TestObjectFactory.ANY_GITHUB_USER_NAME);

        assertThat(retrievedUser.getStatusCode()).isEqualTo(OK);
        assertThat(retrievedUser.getBody()).isEqualTo(userToRetrieve);
    }

    @Test
    public void getUserByUsername_doesNotReturnAnyUser_whenUserDoesNotExistWithGivenUsername() {
        when(userService.getUserByUsername(TestObjectFactory.ANY_GITHUB_USER_NAME)).thenReturn(empty());

        final ResponseEntity<User> retrievedUser = userController.getUserByUsername(TestObjectFactory.ANY_GITHUB_USER_NAME);

        assertThat(retrievedUser.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(retrievedUser.getBody()).isNull();
    }

    @Test
    public void getUsers_returnsUsers_whenUsersExist() {
        final User[] usersToRetrieve = TestObjectFactory.anyGithubUserList(3);

        when(userService.getUsers()).thenReturn(Optional.of(usersToRetrieve));

        final ResponseEntity<User[]> retrievedUsers = userController.getUsers();

        assertThat(retrievedUsers.getStatusCode()).isEqualTo(OK);
        assertThat(retrievedUsers.getBody()).isEqualTo(usersToRetrieve);
    }

    @Test
    public void getUsers_doesNotReturnUsers_whenUsersDoNotExist() {
        when(userService.getUsers()).thenReturn(empty());

        final ResponseEntity<User[]> retrievedUsers = userController.getUsers();

        assertThat(retrievedUsers.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(retrievedUsers.getBody()).isNull();
    }

}
