package tech.jannotti.billing.core.api.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import tech.jannotti.billing.core.api.web.security.filters.AuthenticationFilter;
import tech.jannotti.billing.core.api.web.security.filters.AuthorizationFilter;
import tech.jannotti.billing.core.rest.security.AbstractSecurityConfiguration;
import tech.jannotti.billing.core.rest.security.ResponseParser;
import tech.jannotti.billing.core.rest.security.exception.AccessDeniedHandlerImpl;
import tech.jannotti.billing.core.rest.security.exception.AuthenticationEntryPointImpl;
import tech.jannotti.billing.core.services.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends AbstractSecurityConfiguration {

    @Value("${api.jwt.secret}")
    private String tokenSecret;

    @Value("${api.jwt.expiration}")
    private Integer tokenExpiration;

    @Autowired
    private ResponseParser responseParser;

    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //@formatter:off
        http
            .cors()
        .and()
            .csrf()
                .disable()
            .authorizeRequests()
                .anyRequest().authenticated()
        .and()
            .addFilter(new AuthenticationFilter(authenticationManager(), userService, tokenSecret, tokenExpiration, responseParser))
            .addFilter(new AuthorizationFilter(authenticationManager(), tokenSecret))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                .accessDeniedHandler(new AccessDeniedHandlerImpl());
        //@formatter:off
    }

}
