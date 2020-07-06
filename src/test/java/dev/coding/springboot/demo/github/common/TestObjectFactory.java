package dev.coding.springboot.demo.github.common;

import dev.coding.springboot.demo.github.domain.GithubUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TestObjectFactory {

    public static final String ANY_GITHUB_USER_ID = "anyUserId";
    public static final String ANY_GITHUB_USER_NAME = "anyUserName";

    public static GithubUser[] anyGithubUserList(final int userCount) {
        final GithubUser[] users = new GithubUser[userCount];
        for (int i = 0; i < userCount; i++) {
            users[i] = anyGithubUser(
                    format("%s-%s", ANY_GITHUB_USER_ID, i),
                    format("%s-%s", ANY_GITHUB_USER_NAME, i)
            );
        }
        return users;
    }

    public static GithubUser anyGithubUser(final String id, final String login) {
        final GithubUser user = new GithubUser();
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
