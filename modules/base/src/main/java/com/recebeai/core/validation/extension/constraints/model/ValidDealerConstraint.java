package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealer;

public class ValidDealerConstraint implements ConstraintValidator<ValidDealer, String> {

    @Autowired
    private DealerRepository dealerRepository;

    @Override
    public void initialize(ValidDealer annotation) {
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(token))
            return true;

        // TODO Ver como incluir a company nessa busca
        return dealerRepository.existsByToken(token);
    }

}
