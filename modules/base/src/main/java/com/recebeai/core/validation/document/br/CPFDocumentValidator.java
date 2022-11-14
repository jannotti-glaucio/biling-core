package tech.jannotti.billing.core.validation.document.br;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.validation.document.AbstractDocumentValidator;

@Component("documentValidators.cpfBR")
public class CPFDocumentValidator extends AbstractDocumentValidator {

    private static final int[] cpfWeight = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

    public boolean validate(String documentNumber) {

        if (StringUtils.isBlank(documentNumber) || (documentNumber.length() != 11))
            return false;

        Integer digit1 = calculateModule11Digit(documentNumber.substring(0, 9), cpfWeight);
        Integer digit2 = calculateModule11Digit(documentNumber.substring(0, 9) + digit1, cpfWeight);
        return documentNumber.equals(documentNumber.substring(0, 9) + digit1.toString() + digit2.toString());
    }

}