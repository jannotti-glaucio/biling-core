package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model;

import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.rest.security.util.SecurityHelper;

public abstract class AbstractToModelConverter<D> extends AbstractConverter<String, D> {

    @Autowired
    private SecurityHelper securityHelper;

    protected BaseCompany getLoggedCompany() {

        if (securityHelper.isLoggedDealerUser()) {
            BaseDealer dealer = securityHelper.getLoggedDealer();
            return dealer.getCompany();
        } else {
            return securityHelper.getLoggedCompany();
        }
    }

    protected BaseDealer getLoggedDealer() {

        if (securityHelper.isLoggedApplication()) {
            BaseApplication application = securityHelper.getLoggedApplication();
            return application.getDealer();
        } else {
            return securityHelper.getLoggedDealer();
        }
    }

    protected BaseApplication getLoggedApplication() {
        return securityHelper.getLoggedApplication();
    }

}
