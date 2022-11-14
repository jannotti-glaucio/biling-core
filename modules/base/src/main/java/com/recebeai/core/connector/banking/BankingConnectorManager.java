package tech.jannotti.billing.core.connector.banking;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.connector.exception.ConnectorNotFoundException;

@Component
public class BankingConnectorManager {

    @Autowired
    private ApplicationContext context;

    public BankingConnector getConnector(String connectorId) throws ConnectorNotFoundException {

        Object bean = null;
        try {
            bean = context.getBean(connectorId);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ConnectorNotFoundException("Conector [" + connectorId + "] nao encontrado");
        }

        if (!(bean instanceof BankingConnector))
            throw new ConnectorNotFoundException("Bean [" + connectorId + "] nao eh do tipo BankConnector ["
                + bean.getClass() + "]");

        BankingConnector bankConnector = (BankingConnector) bean;
        return bankConnector;
    }

}
