package tech.jannotti.billing.core.api.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.controllers.AbstractController;

@CrossOrigin
public abstract class AbstractWebController extends AbstractController {

    @Autowired
    protected RestDTOMapper dtoMapper;

    protected BaseUser getLoggedUser() {
        return securityHelper.getLoggedUser();
    }

    protected BaseCompany getLoggedCompany() {
        return securityHelper.getLoggedCompany();
    }

    protected BaseDealer getLoggedDealer() {
        return securityHelper.getLoggedDealer();
    }

}
