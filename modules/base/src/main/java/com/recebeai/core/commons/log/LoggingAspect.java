package tech.jannotti.billing.core.commons.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private LogManagerPool logManagerPool;

    private LogManager getLogManager(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        LogManager logManager = logManagerPool.getLogManager(clazz);
        return logManager;
    }

    @Pointcut("@annotation(tech.jannotti.billing.core.commons.log.annotations.InfoLogging)")
    public void infoLogging() {
    }

    @Pointcut("@annotation(tech.jannotti.billing.core.commons.log.annotations.DebugLogging)")
    public void debugLogging() {
    }

    @Before("infoLogging()")
    public void infoLogging(JoinPoint joinPoint) {
        logBefore(joinPoint, LogLevelEnum.INFO);
    }

    @Before("debugLogging()")
    public void debugLogging(JoinPoint joinPoint) {
        logBefore(joinPoint, LogLevelEnum.DEBUG);
    }

    private void logBefore(JoinPoint joinPoint, LogLevelEnum logLevel) {

        LogManager logManager = getLogManager(joinPoint);

        if (logLevel.equals(LogLevelEnum.DEBUG) && !logManager.isDebugEnabled())
            return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();

        Object[] args = joinPoint.getArgs();

        String parameters = "";
        for (int i = 0; i < paramNames.length; i++) {
            parameters += paramNames[i] + " = " + args[i] + (i != (paramNames.length - 1) ? ", " : "");
        }

        String methodName = joinPoint.getSignature().getName();

        if (logLevel.equals(LogLevelEnum.DEBUG))
            logManager.logDEBUG("Iniciando execucao do metodo %s(%s)", methodName, parameters);
        else
            logManager.logINFO("Iniciando execucao do metodo %s(%s)", methodName, parameters);
    }

    @AfterReturning(pointcut = "infoLogging()", returning = "result")
    public void infoLogging(JoinPoint joinPoint, Object result) {
        logAfter(joinPoint, result, LogLevelEnum.INFO);
    }

    @AfterReturning(pointcut = "debugLogging()", returning = "result")
    public void debugLogging(JoinPoint joinPoint, Object result) {
        logAfter(joinPoint, result, LogLevelEnum.DEBUG);
    }

    public void logAfter(JoinPoint joinPoint, Object result, LogLevelEnum logLevel) {

        LogManager logManager = getLogManager(joinPoint);

        if (logLevel.equals(LogLevelEnum.DEBUG) && !logManager.isDebugEnabled())
            return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();

        String parameters = "";
        for (int i = 0; i < paramNames.length; i++) {
            parameters += paramNames[i] + (i != (paramNames.length - 1) ? ", " : "");
        }

        String methodName = joinPoint.getSignature().getName();

        if (logLevel.equals(LogLevelEnum.DEBUG))
            logManager.logDEBUG("Finalizando execução do metodo %s(%s) com retorno = [%s]", methodName, parameters,
                result);
        else
            logManager.logINFO("Finalizando execução do metodo %s(%s) com retorno = [%s]", methodName, parameters,
                result);
    }

}
