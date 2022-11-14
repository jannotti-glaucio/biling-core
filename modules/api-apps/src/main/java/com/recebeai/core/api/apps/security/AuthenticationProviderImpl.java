package tech.jannotti.billing.core.api.apps.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.services.ApplicationService;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private ApplicationService applicationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken userAuthentication = (UsernamePasswordAuthenticationToken) authentication;

        BaseApplication application = applicationService.get(userAuthentication.getName());
        if (application == null)
            throw new BadCredentialsException("ClientId ou clientSecret nao localizados");

        if (!applicationService.isActive(application))
            throw new BadCredentialsException("ClientId ou clientSecret nao localizados");

        String clientSecret = (String) userAuthentication.getCredentials();
        if (!applicationService.compareClientSecret(application, clientSecret))
            throw new BadCredentialsException("ClientId ou clientSecret nao localizados");

        return new UsernamePasswordAuthenticationToken(application.getClientId(), application.getClientSecret(),
            Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
