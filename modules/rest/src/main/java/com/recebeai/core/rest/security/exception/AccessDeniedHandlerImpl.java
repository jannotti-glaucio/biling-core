package tech.jannotti.billing.core.rest.security.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import tech.jannotti.billing.core.commons.http.HttpHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private LogManager logManager = LogFactory.getManager(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException cause)
        throws IOException, ServletException {

    	String url = request.getRequestURL().toString();
    	String ip = HttpHelper.getRemoteAddr(request);
        logManager.logINFO("Acesso nao autorizado de [%s] para [%s]", ip, url); 

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
