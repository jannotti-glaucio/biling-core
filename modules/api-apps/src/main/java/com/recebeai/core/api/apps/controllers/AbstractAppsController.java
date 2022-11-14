package tech.jannotti.billing.core.api.apps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import tech.jannotti.billing.core.api.apps.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.rest.controllers.AbstractController;

@CrossOrigin
public abstract class AbstractAppsController extends AbstractController {

    @Autowired
    protected RestDTOMapper dtoMapper;

    protected BaseApplication getLoggedApplication() {
        return securityHelper.getLoggedApplication();
    }

}
