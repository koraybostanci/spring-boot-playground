package dev.coding.springboot.demo.github.common;

import dev.coding.springboot.demo.github.domain.GithubUser;

import static java.lang.String.format;

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
}
