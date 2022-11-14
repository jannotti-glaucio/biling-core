package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.CountryRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCountry;

public class ValidCountryConstraint implements ConstraintValidator<ValidCountry, String> {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public void initialize(ValidCountry annotation) {
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(code))
            return true;

        return countryRepository.existsByCode(code.toUpperCase());
    }

}