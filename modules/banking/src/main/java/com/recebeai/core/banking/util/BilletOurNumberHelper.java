package tech.jannotti.billing.core.banking.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.banking.exception.BankingException;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

public class BilletOurNumberHelper {

    public static Integer getOurNumberDigits(BasePaymentBankBillet bankBillet) {
        BaseBank bank = bankBillet.getCompanyBankAccount().getBank();

        if (bank.getBilletOurNumberDigits() > 0) {
            String ourNumber = bankBillet.getOurNumber().toString();
            String ourNumberDigits = ourNumber.substring(ourNumber.length() - bank.getBilletOurNumberDigits());
            return Integer.valueOf(ourNumberDigits);

        } else
            return null;
    }

    public static Long getOurNumberWihtoutDigits(BasePaymentBankBillet bankBillet) {
        BaseBank bank = bankBillet.getCompanyBankAccount().getBank();

        if (bank.getBilletOurNumberDigits() > 0) {
            String ourNumber = bankBillet.getOurNumber().toString();
            String OurNumberWihtoutDigit = ourNumber.substring(0, ourNumber.length() - bank.getBilletOurNumberDigits());
            return Long.valueOf(OurNumberWihtoutDigit);

        } else
            return bankBillet.getOurNumber();
    }

    public static String formatOutNumberToPrinting(BasePaymentBankBillet bankBillet) {
        BaseBank bank = bankBillet.getCompanyBankAccount().getBank();

        if (StringUtils.isNotBlank(bank.getBilletOurNumberPrintMask())) {
            Pattern pattern = Pattern.compile(bank.getBilletOurNumberPrintMask());
            Matcher matcher = pattern.matcher(bankBillet.getOurNumber().toString());
            if (matcher.find())
                return matcher.group();
            else {
                throw new BankingException(
                    "Campo nossoNumero fora do padrao da mascara de impressao [token=%s, ourNumber=%s, ourNumberPrintMask=%s]",
                    bankBillet.getToken(), bankBillet.getOurNumber(), bank.getBilletOurNumberPrintMask());
            }
        } else
            return getOurNumberWihtoutDigits(bankBillet).toString();
    }

}
