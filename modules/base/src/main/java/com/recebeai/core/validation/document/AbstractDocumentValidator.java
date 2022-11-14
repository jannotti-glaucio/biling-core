package tech.jannotti.billing.core.validation.document;

public abstract class AbstractDocumentValidator implements DocumentValidator {

    protected static int calculateModule11Digit(String str, int[] weight) {
        int sum = 0;
        for (int index = str.length() - 1, digit; index >= 0; index--) {
            digit = Integer.parseInt(str.substring(index, index + 1));
            sum += digit * weight[weight.length - str.length() + index];
        }
        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }

}