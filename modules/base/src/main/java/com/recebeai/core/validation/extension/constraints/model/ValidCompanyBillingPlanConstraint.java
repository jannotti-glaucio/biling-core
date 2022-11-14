package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBillingPlanRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCompanyBillingPlan;

public class ValidCompanyBillingPlanConstraint implements ConstraintValidator<ValidCompanyBillingPlan, String> {

    @Autowired
    private CompanyBillingPlanRepository billingPlanRepository;

    @Override
    public void initialize(ValidCompanyBillingPlan annotation) {
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(token))
            return true;

        // TODO Ver como incluir a company logada nessa busca
        return billingPlanRepository.existsByToken(token);
    }

}
