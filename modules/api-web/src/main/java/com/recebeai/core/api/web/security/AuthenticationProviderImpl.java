package tech.jannotti.billing.core.api.web.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.services.user.UserService;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken userAuthentication = (UsernamePasswordAuthenticationToken) authentication;

        BaseUser user = userService.get(userAuthentication.getName());
        if (user == null)
            throw new BadCredentialsException("Username ou password nao localizados");

        if (!userService.isActive(user))
            throw new BadCredentialsException("Username ou password nao localizados");

        String clientSecret = (String) userAuthentication.getCredentials();
        if (!userService.comparePassword(user, clientSecret))
            throw new BadCredentialsException("Username ou password nao localizados");

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
