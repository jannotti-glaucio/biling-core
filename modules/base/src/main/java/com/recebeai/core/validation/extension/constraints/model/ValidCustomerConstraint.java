package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.customer.CustomerRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCustomer;

public class ValidCustomerConstraint implements ConstraintValidator<ValidCustomer, String> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void initialize(ValidCustomer annotation) {
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(token))
            return true;

        // TODO Ver como incluir o dealer nessa busca
        return customerRepository.existsByToken(token);
    }

}
