package tech.jannotti.billing.core.api.web.security.filters;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tech.jannotti.billing.core.api.web.controllers.dto.request.login.LoginRestRequest;
import tech.jannotti.billing.core.commons.bean.parser.BeanParserException;
import tech.jannotti.billing.core.commons.http.HttpConstants;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.security.ResponseParser;
import tech.jannotti.billing.core.rest.security.util.JWTHelper;
import tech.jannotti.billing.core.services.user.UserService;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private LogManager logManager = LogFactory.getManager(AuthenticationFilter.class);

    private AuthenticationManager authenticationManager;
    private UserService userService;

    private String tokenSecret;
    private Integer tokenExpiration;

    private ResponseParser responseParser;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, String tokenSecret,
        int tokenExpiration, ResponseParser responseParser) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenSecret = tokenSecret;
        this.tokenExpiration = tokenExpiration;
        this.responseParser = responseParser;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        LoginRestRequest login = null;
        try {
            login = responseParser.parseResponse(LoginRestRequest.class, request.getInputStream());
        } catch (BeanParserException | IOException e) {
            logManager.logERROR("Erro lendo body JSON", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            login.getUsername(), login.getPassword(), Collections.emptyList());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authentication) throws IOException, ServletException {

        String username = (String) authentication.getPrincipal();
        BaseUser user = userService.get(username);

        String token = JWTHelper.buildToken(user, tokenSecret, tokenExpiration);

        response.addHeader(HttpHeaders.AUTHORIZATION, HttpConstants.BEARER_AUHTORIZATION_PREFIX + token);
    }

}
