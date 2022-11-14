package tech.jannotti.billing.core.api.web.security.filters;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import tech.jannotti.billing.core.commons.http.HttpConstants;
import tech.jannotti.billing.core.rest.security.util.JWTHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private String tokenSecret;

    public AuthorizationFilter(AuthenticationManager authManager, String tokenSecret) {
        super(authManager);
        this.tokenSecret = tokenSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = getAuthentication(request, response);
        } catch (AuthenticationException e) {
            onUnsuccessfulAuthentication(request, response, e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorization))
            throw new AuthenticationCredentialsNotFoundException("Cabecalho Authorization nao enviado");

        String token = authorization.replace(HttpConstants.BEARER_AUHTORIZATION_PREFIX, "");

        Jws<Claims> jws = JWTHelper.parseToken(token, tokenSecret);

        String user = jws.getBody().getSubject();
        if (StringUtils.isBlank(user))
            throw new AuthenticationCredentialsNotFoundException("Token sem dados de usuario");

        // TODO Pensar numa forma de validar se o usuario ainda e valido

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

}
