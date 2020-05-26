package dev.coding.springboot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String HEALTH = "/health";
    private static final String INFO = "/info";
    private static final String METRICS = "/metrics";
    private static final String PROMETHEUS = "/prometheus";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(NEVER)
                .and()
                .rememberMe()
                .disable()
                .authorizeRequests()
                .antMatchers(HEALTH, INFO, METRICS, PROMETHEUS)
                .permitAll();
    }
}
