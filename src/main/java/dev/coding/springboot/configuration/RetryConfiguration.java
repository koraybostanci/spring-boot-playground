package dev.coding.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
//@EnableRetry
public class RetryConfiguration {

    private static final int MAX_ATTEMPTS = 3;
    private static final int MULTIPLIER = 2;
    private static final int INITIAL_INTERVAL = 300;
    private static final int MAX_INTERVAL = 3000;

    @Bean
    public RetryTemplate retryTemplate() {
        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);

        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
