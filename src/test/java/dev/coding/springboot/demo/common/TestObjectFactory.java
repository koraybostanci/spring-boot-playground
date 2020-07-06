package dev.coding.springboot.demo.common;

import dev.coding.springboot.demo.domain.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TestObjectFactory {

    public static final String ANY_GITHUB_USER_ID = "anyUserId";
    public static final String ANY_GITHUB_USER_NAME = "anyUserName";

    public static User[] anyGithubUserList(final int userCount) {
        final User[] users = new User[userCount];
        for (int i = 0; i < userCount; i++) {
            users[i] = anyGithubUser(
                    format("%s-%s", ANY_GITHUB_USER_ID, i),
                    format("%s-%s", ANY_GITHUB_USER_NAME, i)
            );
        }
        return users;
    }

    public static User anyGithubUser(final String id, final String login) {
        final User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setUrl(login);
        return user;
    }

    public static HttpEntity buildHttpEntity() {
        return new HttpEntity(buildHttpHeaders());
    }

    public static HttpHeaders buildHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));

        return httpHeaders;
    }
}
