package tech.jannotti.billing.core.rest.security.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import tech.jannotti.billing.core.commons.http.HttpHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private LogManager logManager = LogFactory.getManager(AuthenticationEntryPointImpl.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException cause)
        throws IOException, ServletException {
    	
        if (cause instanceof InsufficientAuthenticationException) {
        	String url = request.getRequestURL().toString();
        	String ip = HttpHelper.getRemoteAddr(request);
            logManager.logINFO("Acesso nao autorizado de [%s] para [%s]", ip, url);

        } else
            logManager.logERROR("Erro efetuando autenticacao", cause);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
