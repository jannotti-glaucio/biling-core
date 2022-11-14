package tech.jannotti.billing.core.services.user;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.ModelConstants;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.user.BaseCompanyUser;
import tech.jannotti.billing.core.persistence.model.base.user.BaseDealerUser;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.persistence.repository.base.user.UserRepository;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class UserService extends AbstractUserService {

    @Autowired
    private UserRepository userRepository;

    public BaseCompanyUser castCompanyUser(BaseUser user) {

        if (user instanceof BaseCompanyUser)
            return (BaseCompanyUser) user;
        else
            return null;
    }

    public BaseDealerUser castDealerUser(BaseUser user) {

        if (user instanceof BaseDealerUser)
            return (BaseDealerUser) user;
        else
            return null;
    }

    public boolean isAdminUser(BaseUser user) {
        return (user.getRole().getCode().equals(ModelConstants.ADMIN_ROLE_CODE));
    }

    public boolean isCompanyUser(BaseUser user) {
        BaseCompanyUser companyUser = castCompanyUser(user);

        return ((companyUser != null) && companyUser.getRole().getCode().equals(ModelConstants.COMPANY_ROLE_CODE)
            && (companyUser.getCompany() != null));
    }

    public boolean isDealerUser(BaseUser user) {
        BaseDealerUser dealerUser = castDealerUser(user);

        return ((dealerUser != null) && dealerUser.getRole().getCode().equals(ModelConstants.DEALER_ROLE_CODE)
            && (dealerUser.getDealer() != null));
    }

    public BaseUser get(String username) {
        return userRepository.getByUsername(username);
    }

    public boolean isActive(BaseUser user) {

        if (!user.getStatus().equals(EntityStatusEnum.ACTIVE))
            return false;

        if (isDealerUser(user)) {
            BaseDealerUser dealerUser = castDealerUser(user);
            if (!dealerUser.getDealer().getStatus().equals(EntityStatusEnum.ACTIVE))
                return false;
            if (!dealerUser.getDealer().getCompany().getStatus().equals(EntityStatusEnum.ACTIVE))
                return false;

        } else if (isCompanyUser(user)) {
            BaseCompanyUser companyUser = castCompanyUser(user);
            if (!companyUser.getCompany().getStatus().equals(EntityStatusEnum.ACTIVE))
                return false;
        }

        return true;
    }

    public Map<String, Object> getEnvironment(BaseUser user) {
        Map<String, Object> environment = new HashMap<String, Object>();

        if (isDealerUser(user)) {
            BaseDealerUser dealerUser = castDealerUser(user);

            BaseDealer dealer = dealerUser.getDealer();

            BaseCompanyBillingPlan billingPlan = dealer.getBillingPlan();
            environment.put("paidBankBilletFee", billingPlan.getPaidBankBilletFee());
            environment.put("marketWithdrawFee", billingPlan.getMarketWithdrawFee());

            BaseCompany company = dealer.getCompany();
            environment.put("minimumInvoiceAmount", company.getMinimumInvoiceAmount());
            environment.put("maximumInvoiceAmount", company.getMaximumInvoiceAmount());
            environment.put("minimumMarketWithdrawAmount", company.getMinimumMarketWithdrawAmount());
            environment.put("maximumMarketWithdrawAmount", company.getMaximumMarketWithdrawAmount());
        }
        return environment;
    }

    public boolean comparePassword(BaseUser user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public void updatePassword(BaseUser user, String currentPassword, String newPassword) {

        // Valida se a senha atual estah correta
        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            throw new ResultCodeServiceException(CODE_INVALID_CURRENT_PASSWORD);

        updatePassword(user, newPassword);
    }

}
