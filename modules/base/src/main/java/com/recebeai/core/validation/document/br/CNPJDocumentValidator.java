package tech.jannotti.billing.core.validation.document.br;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.validation.document.AbstractDocumentValidator;

@Component("documentValidators.cnpjBR")
public class CNPJDocumentValidator extends AbstractDocumentValidator {

    private static final int[] cnpjWeight = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

    public boolean validate(String documentNumber) {

        if (StringUtils.isBlank(documentNumber) || (documentNumber.length() != 14))
            return false;

        Integer digit1 = calculateModule11Digit(documentNumber.substring(0, 12), cnpjWeight);
        Integer digit2 = calculateModule11Digit(documentNumber.substring(0, 12) + digit1, cnpjWeight);
        return documentNumber.equals(documentNumber.substring(0, 12) + digit1.toString() + digit2.toString());
    }

}