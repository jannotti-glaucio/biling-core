package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBankAccountRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCompanyBankAccount;

public class ValidCompanyBankAccountConstraint implements ConstraintValidator<ValidCompanyBankAccount, String> {

    @Autowired
    private CompanyBankAccountRepository bankAccountRepository;

    @Override
    public void initialize(ValidCompanyBankAccount annotation) {
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(token))
            return true;

        // TODO Ver como incluir a company logada nessa busca
        return bankAccountRepository.existsByToken(token);
    }

}
