package tech.jannotti.billing.core.services.company;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBillingPlanRepository;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.dto.request.BillingPlanServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class CompanyBillingPlanService extends AbstractService {

    @Autowired
    private CompanyBillingPlanRepository billingPlanRepository;

    @Autowired
    private DealerRepository dealerRepository;

    public List<BaseCompanyBillingPlan> find(BaseCompany company) {
        return billingPlanRepository.findByCompanyAndStatusNot(company, EntityStatusEnum.DELETED);
    }

    public BaseCompanyBillingPlan get(BaseCompany company, String token) {
        BaseCompanyBillingPlan billingPlan = billingPlanRepository.getByCompanyAndToken(company, token);

        if (billingPlan == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return billingPlan;
    }

    @Transactional
    public BaseCompanyBillingPlan add(BaseCompany company, BillingPlanServiceRequest serviceRequest) {

        BaseCompanyBillingPlan billingPlan = dtoMapper.map(serviceRequest, BaseCompanyBillingPlan.class);
        billingPlan.setCompany(company);
        billingPlan.setToken(generateToken());
        billingPlan.setCreationDate(DateTimeHelper.getNowDateTime());
        billingPlan.setStatus(EntityStatusEnum.ACTIVE);

        billingPlanRepository.save(billingPlan);

        return billingPlan;

    }

    @Transactional
    public BaseCompanyBillingPlan update(BaseCompany company, String token, BillingPlanServiceRequest request) {

        BaseCompanyBillingPlan billingPlan = get(company, token);

        if (billingPlan == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        if (!billingPlan.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(CODE_INVALID_BILLING_PLAN_STATUS_TO_UPDATE);

        dtoMapper.map(request, billingPlan);

        billingPlanRepository.save(billingPlan);

        return billingPlan;
    }

    @Transactional
    public void delete(BaseCompany company, String token) {

        BaseCompanyBillingPlan billingPlan = get(company, token);

        if (billingPlan.getStatus().equals(EntityStatusEnum.DELETED))
            throw new ResultCodeServiceException(CODE_INVALID_BILLING_PLAN_STATUS_TO_DELETE);

        boolean planInUse = dealerRepository.existsByCompanyAndBillingPlanAndStatusNot(company, billingPlan,
            EntityStatusEnum.DELETED);

        if (planInUse)
            throw new ResultCodeServiceException(CODE_COULD_NOT_DELETE_IN_USE_BILLING_PLAN);

        LocalDateTime deletionDate = DateTimeHelper.getNowDateTime();
        billingPlanRepository.updateStatusAndDeletionDateById(billingPlan.getId(), EntityStatusEnum.DELETED, deletionDate);
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("companyBillingPlan.token", 4);
    }
}
