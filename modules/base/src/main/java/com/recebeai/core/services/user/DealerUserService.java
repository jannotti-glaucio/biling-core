package tech.jannotti.billing.core.services.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.ModelConstants;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.user.BaseDealerUser;
import tech.jannotti.billing.core.persistence.repository.base.user.DealerUserRepository;
import tech.jannotti.billing.core.services.dto.request.user.AddUserServiceRequest;
import tech.jannotti.billing.core.services.dto.request.user.UpdateUserServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class DealerUserService extends AbstractUserEntityService {

    @Autowired
    private DealerUserRepository dealerUserRepository;

    public List<BaseDealerUser> find(BaseDealer dealer) {
        return dealerUserRepository.findByDealerAndFilterAndStatusNot(dealer, EntityStatusEnum.DELETED);
    }

    @Transactional
    public BaseDealerUser add(BaseDealer dealer, AddUserServiceRequest request) {

        BaseDealerUser user = add(BaseDealerUser.class, request, ModelConstants.DEALER_ROLE_CODE);
        user.setDealer(dealer);

        dealerUserRepository.save(user);

        return user;
    }

    public BaseDealerUser get(BaseCompany company, String token) {

        BaseDealerUser user = dealerUserRepository.getByDealerCompanyAndToken(company, token);
        if (user == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return user;
    }

    @Transactional
    public void update(BaseCompany company, String token, UpdateUserServiceRequest request) {

        BaseDealerUser user = get(company, token);
        update(user, request);

        dealerUserRepository.save(user);
    }

    @Transactional
    public void updatePassword(BaseCompany company, String token, String password) {

        BaseDealerUser user = get(company, token);
        updatePassword(user, password);
    }

    @Transactional
    public void block(BaseCompany company, String token) {

        BaseDealerUser user = get(company, token);
        block(user);
    }

    @Transactional
    public void unblock(BaseCompany company, String token) {

        BaseDealerUser user = get(company, token);
        unblock(user);
    }

    @Transactional
    public void delete(BaseCompany company, String token) {

        BaseDealerUser user = get(company, token);
        delete(user);
    }

}
