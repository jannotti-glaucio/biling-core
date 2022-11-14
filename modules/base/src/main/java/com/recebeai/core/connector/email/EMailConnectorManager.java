package tech.jannotti.billing.core.connector.email;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.connector.exception.ConnectorNotFoundException;

@Component
public class EMailConnectorManager {

    @Autowired
    private ApplicationContext context;

    public EMailConnector getConnector(String connectorId) throws ConnectorNotFoundException {

        Object bean = null;
        try {
            bean = context.getBean(connectorId);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ConnectorNotFoundException("Conector [" + connectorId + "] nao encontrado");
        }

        if (!(bean instanceof EMailConnector))
            throw new ConnectorNotFoundException("Bean [" + connectorId + "] nao eh do tipo EMailConnector ["
                + bean.getClass() + "]");

        EMailConnector mailConnector = (EMailConnector) bean;
        return mailConnector;
    }

}
