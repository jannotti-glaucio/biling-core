package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.AbstractToModelConverter;
import tech.jannotti.billing.core.services.company.CompanyBillingPlanService;

@Component
public class ToCompanyBillingPlanConverter extends AbstractToModelConverter<BaseCompanyBillingPlan> {

    @Autowired
    private CompanyBillingPlanService billingPlanService;

    @Override
    protected BaseCompanyBillingPlan convert(String source) {
        if (source == null)
            return null;

        BaseCompany company = getLoggedCompany();
        return billingPlanService.get(company, source);
    }

}
