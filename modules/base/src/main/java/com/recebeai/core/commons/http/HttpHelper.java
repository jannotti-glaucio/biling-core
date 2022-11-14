package tech.jannotti.billing.core.commons.http;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class HttpHelper {

    public static HttpServletRequest getHttpRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
    }
    
    public static String getRemoteAddr(HttpServletRequest request) {
    	
    	String ipAddress = request.getHeader("X-FORWARDED-FOR");  
    	if (ipAddress == null)
    		ipAddress = request.getRemoteAddr();  
    	
    	return ipAddress;
    }

}
