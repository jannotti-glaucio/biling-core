package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerBankAccountRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealerBankAccount;

public class ValidDealerBankAccountConstraint implements ConstraintValidator<ValidDealerBankAccount, String> {

    @Autowired
    private DealerBankAccountRepository bankAccountRepository;

    @Override
    public void initialize(ValidDealerBankAccount annotation) {
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(token))
            return true;

        // TODO Ver como incluir o dealer nessa busca
        return bankAccountRepository.existsByToken(token);
    }

}
