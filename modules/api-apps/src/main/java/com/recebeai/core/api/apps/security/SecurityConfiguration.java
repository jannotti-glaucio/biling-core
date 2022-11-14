package tech.jannotti.billing.core.api.apps.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.security.AbstractSecurityConfiguration;
import tech.jannotti.billing.core.rest.security.exception.AccessDeniedHandlerImpl;
import tech.jannotti.billing.core.rest.security.exception.AuthenticationEntryPointImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends AbstractSecurityConfiguration {

    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

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
            .httpBasic()
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                .accessDeniedHandler(new AccessDeniedHandlerImpl());
        //@formatter:off
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {

        //@formatter:off
        web.ignoring()        
            // Ignora a URL de leitura de Boleto
            .antMatchers(HttpMethod.GET,  ApiConstants.V1_API_PATH + "invoice/bankbillet/*");
        //@formatter:off
    }

}
