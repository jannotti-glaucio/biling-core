package tech.jannotti.billing.core.rest.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.user.BaseCompanyUser;
import tech.jannotti.billing.core.persistence.model.base.user.BaseDealerUser;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.exception.ControllerException;
import tech.jannotti.billing.core.rest.exception.ResultCodeControllerException;
import tech.jannotti.billing.core.services.ApplicationService;
import tech.jannotti.billing.core.services.user.UserService;

@Component
public class SecurityHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    // User

    public BaseUser getLoggedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new ControllerException("Erro carregando sem um usuario logado");

        BaseUser user = userService.get(authentication.getName());

        if (user == null)
            throw new ControllerException("Erro carregando usuario logado [%s]", authentication.getName());
        else
            return user;
    }

    public BaseUser getLoggedAdmin() {
        BaseUser user = getLoggedUser();

        if (!userService.isAdminUser(user))
            throw new ResultCodeControllerException(ResultCodeConstants.CODE_ADMIN_USER_REQUIRED_TO_ACCESS);
        else
            return user;
    }

    public BaseCompany getLoggedCompany() {
        BaseUser user = getLoggedUser();

        if (!userService.isCompanyUser(user))
            throw new ResultCodeControllerException(ResultCodeConstants.CODE_COMPANY_USER_REQUIRED_TO_ACCESS);
        else {
            BaseCompanyUser companyUser = userService.castCompanyUser(user);
            return companyUser.getCompany();
        }
    }

    public BaseDealer getLoggedDealer() {
        BaseUser user = getLoggedUser();

        if (!userService.isDealerUser(user))
            throw new ResultCodeControllerException(ResultCodeConstants.CODE_DEALER_USER_REQUIRED_TO_ACCESS);
        else {
            BaseDealerUser dealerUser = userService.castDealerUser(user);
            return dealerUser.getDealer();
        }
    }

    public boolean isLoggedAdminUser() {
        BaseUser user = getLoggedUser();
        return userService.isAdminUser(user);
    }

    public boolean isLoggedCompanyUser() {
        BaseUser user = getLoggedUser();
        return userService.isCompanyUser(user);
    }

    public boolean isLoggedDealerUser() {
        BaseUser user = getLoggedUser();
        return userService.isDealerUser(user);
    }

    // Application

    public BaseApplication getLoggedApplication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new ControllerException("Erro carregando sem uma aplicacao logada");

        BaseApplication application = applicationService.get(authentication.getName());

        if (application == null)
            throw new ControllerException("Erro carregando aplicacao logada [%s]", authentication.getName());
        else
            return application;
    }

    public boolean isLoggedApplication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new ControllerException("Erro carregando sem uma aplicacao logada");

        BaseApplication application = applicationService.get(authentication.getName());

        if (application != null)
            return true;
        else
            return false;
    }

}
