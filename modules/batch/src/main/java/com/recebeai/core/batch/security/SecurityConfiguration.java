package tech.jannotti.billing.core.batch.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import tech.jannotti.billing.core.rest.security.AbstractSecurityConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends AbstractSecurityConfiguration {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //@formatter:off
        http
            .csrf()
                .disable()
            .authorizeRequests()
                .anyRequest().permitAll();
        //@formatter:off
    }

}
